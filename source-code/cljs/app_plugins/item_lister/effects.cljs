
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.effects
    (:require [app-plugins.item-lister.engine     :as engine]
              [app-plugins.item-lister.events     :as events]
              [app-plugins.item-lister.queries    :as queries]
              [app-plugins.item-lister.subs       :as subs]
              [app-plugins.item-lister.validators :as validators]
              [x.app-core.api                     :as a :refer [r]]
              [x.app-ui.api                       :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
            db           (r events/toggle-reload-mode!               db extension-id item-namespace)
            query        (r queries/get-request-items-query          db extension-id item-namespace)
            validator-f #(r validators/request-items-response-valid? db extension-id item-namespace %)]
           [:sync/send-query! (engine/request-id extension-id item-namespace)
                              {:display-progress? true
                               ; XXX#4057
                               :on-stalled [:item-lister/receive-reloaded-items! extension-id item-namespace]
                               :on-failure [:item-lister/set-error-mode!         extension-id item-namespace]
                               :query query :validator-f validator-f}])))



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
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      {:db (r events/unload-lister! db extension-id item-namespace)
       :dispatch-n [; XXX#5660
                    [:environment/remove-keypress-listener! :item-lister/keypress-listener]]}))



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
          (let [query        (r queries/get-request-items-query          db extension-id item-namespace)
                validator-f #(r validators/request-items-response-valid? db extension-id item-namespace %)]
               [:sync/send-query! (engine/request-id extension-id item-namespace)
                                  {:display-progress? true
                                   ; XXX#4057
                                   ; A letöltött dokumentumok on-success helyett on-stalled időpontban
                                   ; kerülnek tárolásra a Re-Frame adatbázisba, így elkerülhető,
                                   ; hogy a request idle-timeout ideje alatt az újonnan letöltött
                                   ; dokumentumok már kirenderelésre kerüljenek, amíg a letöltést jelző
                                   ; felirat még megjelenik a lista végén.
                                   :on-stalled [:item-lister/receive-items!  extension-id item-namespace]
                                   :on-failure [:item-lister/set-error-mode! extension-id item-namespace]
                                   :query query :validator-f validator-f}]))))

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
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [item-ids     (r subs/get-selected-item-ids              db extension-id item-namespace)
            query        (r queries/get-delete-items-query          db extension-id item-namespace item-ids)
            validator-f #(r validators/delete-items-response-valid? db extension-id item-namespace %)]
           {:db (r events/delete-selected-items! db extension-id item-namespace)
            :dispatch [:sync/send-query! (engine/request-id extension-id item-namespace)
                                         {:on-success [:item-lister/items-deleted       extension-id item-namespace]
                                          :on-failure [:item-lister/delete-items-failed extension-id item-namespace]
                                          :query query :validator-f validator-f}]})))

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
  :item-lister/delete-items-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  (fn [{:keys [db]} [_ extension-id item-namespace _]]
      ; Ha a kijelölt elemek törlése sikertelen volt ...
      ; ... megszűnteti a kijelöléseket
      ; ... engedélyezi az ideiglenesen letiltott elemeket
      ; ... befejezi progress-bar elemen kijelzett folyamatot
      ; ... megjelenít egy értesítést
      {:db (r events/delete-items-failed db extension-id item-namespace)
       :dispatch-n [[:ui/end-fake-process!]
                    [:ui/blow-bubble! {:body :failed-to-delete}]]}))

(a/reg-event-fx
  :item-lister/undo-delete-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  (fn [{:keys [db]} [_ extension-id item-namespace item-ids]]
      (let [query        (r queries/get-undo-delete-items-query          db extension-id item-namespace item-ids)
            validator-f #(r validators/undo-delete-items-response-valid? db extension-id item-namespace %)]
           {:db (r ui/fake-process! db 15)
            :dispatch [:sync/send-query! (engine/request-id extension-id item-namespace)
                                         {:on-success [:item-lister/reload-items!            extension-id item-namespace]
                                          :on-failure [:item-lister/undo-delete-items-failed extension-id item-namespace]
                                          :query query :validator-f validator-f}]})))

(a/reg-event-fx
  :item-lister/undo-delete-items-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  (fn [{:keys [db]} [_ extension-id item-namespace _]]
      ; Ha a kijelölt elemek törlésének visszavonása sikertelen volt ...
      ; ... befejezi progress-bar elemen kijelzett folyamatot
      ; ... megjelenít egy értesítést
      {:dispatch-n [[:ui/end-fake-process!]
                    [:ui/blow-bubble! {:body :failed-to-delete}]]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/duplicate-selected-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [item-ids     (r subs/get-selected-item-ids                 db extension-id item-namespace)
            query        (r queries/get-duplicate-items-query          db extension-id item-namespace item-ids)
            validator-f #(r validators/duplicate-items-response-valid? db extension-id item-namespace %)]
           {:db (r ui/fake-process! db 15)
            :dispatch [:sync/send-query! (engine/request-id extension-id item-namespace)
                                         {:on-success [:item-lister/items-duplicated       extension-id item-namespace]
                                          :on-failure [:item-lister/duplicate-items-failed extension-id item-namespace]
                                          :query query :validator-f validator-f}]})))

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
  :item-lister/duplicate-items-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  (fn [{:keys [db]} [_ extension-id item-namespace _]]
      ; Ha a kijelölt elemek duplikálása sikertelen volt ...
      ; ... befejezi progress-bar elemen kijelzett folyamatot
      ; ... megjelenít egy értesítést
      {:dispatch-n [[:ui/end-fake-process!]
                    [:ui/blow-bubble! {:body :failed-to-duplicate}]]}))

(a/reg-event-fx
  :item-lister/undo-duplicate-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) copy-ids
  (fn [{:keys [db]} [_ extension-id item-namespace copy-ids]]
      (let [query        (r queries/get-undo-duplicate-items-query          db extension-id item-namespace copy-ids)
            validator-f #(r validators/undo-duplicate-items-response-valid? db extension-id item-namespace %)]
           {:db (r ui/fake-process! db 15)
            :dispatch [:sync/send-query! (engine/request-id extension-id item-namespace)
                                         {:on-success [:item-lister/reload-items!               extension-id item-namespace]
                                          :on-failure [:item-lister/undo-duplicate-items-failed extension-id item-namespace]
                                          :query query :validator-f validator-f}]})))

(a/reg-event-fx
  :item-lister/undo-duplicate-items-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  (fn [{:keys [db]} [_ extension-id item-namespace _]]
      ; Ha a kijelölt elemek duplikálásának visszavonása sikertelen volt ...
      ; ... befejezi progress-bar elemen kijelzett folyamatot
      ; ... megjelenít egy értesítést
      {:dispatch-n [[:ui/end-fake-process!]
                    [:ui/blow-bubble! {:body :failed-to-undo-duplicate}]]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/init-body!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;  {:ui-title (metamorphic-content)(opt)}
  (fn [{:keys [db]} [_ extension-id item-namespace {:keys [ui-title] :as body-props}]]
      {:db (r events/init-body! db extension-id item-namespace body-props)
       :dispatch-n [(if ui-title [:ui/set-title! ui-title])
                    ; XXX#5660
                    ; Az :item-lister/keypress-listener biztosítja, hogy a keypress-handler aktív legyen.
                    [:environment/reg-keypress-listener! :item-lister/keypress-listener]]}))

(a/reg-event-fx
  :item-lister/init-header!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  (fn [{:keys [db]} [_ extension-id item-namespace header-props]]
      {:db (r events/init-header! db extension-id item-namespace header-props)}))
