
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.effects
    (:require [x.app-core.api :as a :refer [r]]
              [x.app-ui.api   :as ui]
              [x.app-environment.api           :as environment]
              [app-plugins.item-lister.engine  :as engine]
              [app-plugins.item-lister.events  :as events]
              [app-plugins.item-lister.queries :as queries]
              [app-plugins.item-lister.subs    :as subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/load-lister!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-lister/load-lister! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [route-title (r subs/get-meta-item db extension-id item-namespace :route-title)]
           {:db (r events/load-lister! db extension-id item-namespace)
            :dispatch-n [; XXX#5660
                         ; Az :item-lister/keypress-listener biztosítja, hogy a keypress-handler aktív legyen.
                         [:environment/reg-keypress-listener! :item-lister/keypress-listener]
                         ; XXX#3237
                         ; Ha az item-lister plugin az "/@app-home/my-extension" útvonalon van elindítva,
                         ; akkor feltételezi, hogy a UI-surface az item-lister plugint jeleníti meg, ezért
                         ; beállítja a header-title és window-title feliratokat.
                         (if (r subs/set-title? db extension-id item-namespace)
                             [:ui/set-title! route-title])
                         (engine/load-extension-event extension-id item-namespace)]})))

(a/reg-event-fx
  :item-lister/use-filter!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) filter-pattern
  ;
  ; @usage
  ;  [:item-lister/use-filter! :my-extension :my-type {...}]
  (fn [{:keys [db]} [_ extension-id item-namespace filter-pattern]]
      {:db (r events/use-filter! db extension-id item-namespace filter-pattern)
       :dispatch [:tools/reload-infinite-loader! extension-id]}))

(a/reg-event-fx
  :item-lister/reload-items!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-lister/reload-items! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      ; - Az [:item-lister/reload-items! ...] esemény újra letölti a listában található elemeket.
      ; - Ha a szerver-oldalon az elemeket tartalmazó kollekció megváltozott, akkor nem feltétlenül
      ;   ugyanazok az elemek töltődnek le!
      ; - Ha pl. a kliens-oldalon az újratöltés előtt 42 elem van letöltve és a {:download-limit ...}
      ;   értéke 20, akkor az esemény az 1. - 60. elemeket kéri le a szerverről.
      (let [; A {:reload-mode? true} beállítás a query elkészítéséhez szükséges, utána nincs szükség
            ; rá, hogy érvényben maradjon, ezért nincs eltárolva!
            db (r events/toggle-reload-mode! db extension-id item-namespace)]
           [:sync/send-query! (engine/request-id extension-id item-namespace)
                              {:display-progress? true
                               ; XXX#4057
                               :on-stalled   [:item-lister/receive-reloaded-items! extension-id item-namespace]
                               :on-failure   [:item-lister/set-error-mode!         extension-id item-namespace]
                               :query        (r queries/get-request-items-query       db extension-id item-namespace)
                               :validator-f #(r queries/request-items-response-valid? db extension-id item-namespace %)}])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/search-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      {:db (r events/reset-downloads! db extension-id item-namespace)
       :dispatch [:tools/reload-infinite-loader! extension-id]}))

(a/reg-event-fx
  :item-lister/order-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      {:db (r events/reset-downloads! db extension-id item-namespace)
       :dispatch [:tools/reload-infinite-loader! extension-id]}))

(a/reg-event-fx
  :item-lister/unload-lister!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; XXX#5660
  [:environment/remove-keypress-listener! :item-lister/keypress-listener])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/request-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (if ; Ha az infinite-loader komponens ismételten megjelenik a viewport területén, csak abban
          ; az esetben próbáljon újabb elemeket letölteni, ha még nincs az összes letöltve.
          (r subs/request-items? db extension-id item-namespace)
          [:sync/send-query! (engine/request-id extension-id item-namespace)
                             {:display-progress? true
                              ; XXX#4057
                              ; A letöltött dokumentumok on-success helyett on-stalled időpontban
                              ; kerülnek tárolásra a Re-Frame adatbázisba, így elkerülhető,
                              ; hogy a request idle-timeout ideje alatt az újonnan letöltött
                              ; dokumentumok már kirenderelésre kerüljenek, amíg a letöltést jelző
                              ; felirat még megjelenik a lista végén.
                              :on-stalled   [:item-lister/receive-items!  extension-id item-namespace]
                              :on-failure   [:item-lister/set-error-mode! extension-id item-namespace]
                              :query        (r queries/get-request-items-query       db extension-id item-namespace)
                              :validator-f #(r queries/request-items-response-valid? db extension-id item-namespace %)}])))

(a/reg-event-fx
  :item-lister/receive-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  (fn [{:keys [db]} [_ extension-id item-namespace server-response]]
      {:db (r events/receive-items! db extension-id item-namespace server-response)
       ; Az elemek letöltődése után újratölti az infinite-loader komponenst, hogy megállapítsa,
       ; hogy az a viewport területén van-e még és szükséges-e további elemeket letölteni.
       :dispatch [:tools/reload-infinite-loader! extension-id]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/delete-selected-items!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-lister/delete-selected-items! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      {:db (r events/delete-selected-items! db extension-id item-namespace)
       :dispatch [:sync/send-query! (engine/request-id extension-id item-namespace)
                                    {:on-success [:item-lister/items-deleted extension-id item-namespace]
                                     :on-failure [:ui/blow-bubble! {:body :failed-to-delete}]
                                     :query      (r queries/get-delete-selected-items-query db extension-id item-namespace)}]}))

(a/reg-event-fx
  :item-lister/items-deleted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  (fn [{:keys [db]} [_ extension-id item-namespace server-response]]
      (let [item-ids (engine/server-response->deleted-item-ids extension-id item-namespace server-response)]
           {:dispatch-n [[:item-lister/render-items-deleted-dialog! extension-id item-namespace item-ids]
                         [:item-lister/reload-items!                extension-id item-namespace]]})))

(a/reg-event-fx
  :item-lister/undo-delete-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  (fn [{:keys [db]} [_ extension-id item-namespace item-ids]]
      {:db (r ui/fake-random-process! db)
       :dispatch [:sync/send-query! (engine/request-id extension-id item-namespace)
                                    {:on-success [:item-lister/reload-items! extension-id item-namespace]
                                     :on-failure [:ui/blow-bubble! {:body :failed-to-undo-delete}]
                                     :query      (r queries/get-undo-delete-items-query db extension-id item-namespace item-ids)}]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/duplicate-selected-items!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-lister/duplicate-selected-items! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      {:db (r ui/fake-random-process! db)
       :dispatch [:sync/send-query! (engine/request-id extension-id item-namespace)
                                    {:on-success [:item-lister/items-duplicated extension-id item-namespace]
                                     :on-failure [:ui/blow-bubble! {:body :failed-to-duplicate}]
                                     :query      (r queries/get-duplicate-selected-items-query db extension-id item-namespace)}]}))

(a/reg-event-fx
  :item-lister/items-duplicated
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  (fn [{:keys [db]} [_ extension-id item-namespace server-response]]
      (let [copy-ids (engine/server-response->duplicated-item-ids extension-id item-namespace server-response)]
           {:dispatch-n [[:item-lister/render-items-duplicated-dialog! extension-id item-namespace copy-ids]
                         [:item-lister/reload-items!                   extension-id item-namespace]]})))

(a/reg-event-fx
  :item-lister/undo-duplicate-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) copy-ids
  (fn [{:keys [db]} [_ extension-id item-namespace copy-ids]]
      {:db (r ui/fake-random-process! db)
       :dispatch [:sync/send-query! (engine/request-id extension-id item-namespace)
                                    {:on-success [:item-lister/reload-items! extension-id item-namespace]
                                     :on-failure [:ui/blow-bubble! {:body :failed-to-undo-duplicate}]
                                     :query      (r queries/get-undo-duplicate-items-query db extension-id item-namespace copy-ids)}]}))
