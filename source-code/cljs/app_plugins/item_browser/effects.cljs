

;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-browser.effects
    (:require [x.app-core.api :as a :refer [r]]
              [x.app-ui.api   :as ui]
              [app-plugins.item-browser.engine     :as engine]
              [app-plugins.item-browser.events     :as events]
              [app-plugins.item-browser.queries    :as queries]
              [app-plugins.item-browser.subs       :as subs]
              [app-plugins.item-browser.validators :as validators]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/load-browser!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) browser-props
  ;  {:item-id (string)(opt)}
  ;
  ; @usage
  ;  [:item-browser/load-browser! :my-extension :my-type]
  ;
  ; @usage
  ;  [:item-browser/load-browser! :my-extension :my-type {...}]
  ;
  ; @usage
  ;  [:item-browser/load-browser! :my-extension :my-type {:item-id "my-item"}]
  (fn [{:keys [db]} [_ extension-id item-namespace browser-props]]
      {:db (r events/load-browser! db extension-id item-namespace browser-props)
       :dispatch-n [; XXX#5660
                    [:environment/reg-keypress-listener! :item-browser/keypress-listener]
                    [:item-browser/request-item! extension-id item-namespace]
                    (engine/load-extension-event extension-id item-namespace)
                    ; Ha az [:item-browser/load-browser! ...] esemény megtörténése előtt is
                    ; meg volt jelenítve az item-browser/body komponens és az infinite-loader
                    ; komponens a viewport területén volt, akkor szükséges az infinite-loader
                    ; komponenst újratölteni, hogy a megváltozott beállításokkal újratöltse
                    ; az adatokat.
                    [:tools/reload-infinite-loader! extension-id]]}))

(a/reg-event-fx
  :item-browser/browse-item!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @usage
  ;  [:item-browser/browse-item! :my-extension :my-type "my-item"]
  (fn [{:keys [db]} [_ extension-id item-namespace item-id]]
      ; - Az [:item-browser/browse-item! ...] esemény nem vizsglja, hogy az item-browser plugin
      ;   útvonala létezik-e.
      ; - Ha az aktuális útvonal az item-browser plugin útvonala, akkor átirányít a böngészendő
      ;   elem útvonalára.
      ; - Ha az aktuális útvonal NEM az item-browser plugin útvonala, akkor útvonal használata
      ;   nélkül indítja el az item-browser plugint, ezért lehetséges a plugint útvonalak
      ;   használata nélkül is elindítani, akkor is ha az item-browser plugin útvonalai léteznek.
      ;   Pl.: A plugin popup elemen való megjelenítése, útvonalak használata nélkül ...
      (if (r subs/route-handled? db extension-id item-namespace)
          ; If handled by route ...
          (let [browser-uri (engine/browser-uri extension-id item-namespace item-id)]
               [:router/go-to! browser-uri])
          ; If NOT handled by route ...
          [:item-browser/load-browser! extension-id item-namespace {:item-id item-id}])))

(a/reg-event-fx
  :item-browser/reload-items!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-browser/reload-items! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      {:dispatch-n [[:item-lister/reload-items!  extension-id item-namespace]
                    [:item-browser/request-item! extension-id item-namespace]]}))

(a/reg-event-fx
  :item-browser/go-home!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-browser/go-home! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [root-item-id (r subs/get-root-item-id db extension-id item-namespace)]
          [:item-browser/browse-item! extension-id item-namespace root-item-id])))

(a/reg-event-fx
  :item-browser/go-up!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-browser/go-up! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [parent-item-id (r subs/get-parent-item-id db extension-id item-namespace)]
          [:item-browser/browse-item! extension-id item-namespace parent-item-id])))

(a/reg-event-fx
  :item-browser/use-filter!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) filter-pattern
  ;
  ; @usage
  ;  [:item-browser/use-filter! :my-extension :my-type {...}]
  (fn [{:keys [db]} [_ extension-id item-namespace filter-pattern]]
      {:db (r events/use-filter! db extension-id item-namespace filter-pattern)
       :dispatch [:tools/reload-infinite-loader! extension-id]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/request-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [query        (r queries/get-request-item-query          db extension-id item-namespace)
            validator-f #(r validators/request-item-response-valid? db extension-id item-namespace %)]
           [:sync/send-query! (engine/request-id extension-id item-namespace)
                              {:on-failure [:item-browser/set-error-mode! extension-id item-namespace]
                               :on-success [:item-browser/receive-item!   extension-id item-namespace]
                               :query query :validator-f validator-f}])))

(a/reg-event-fx
  :item-browser/receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  (fn [{:keys [db]} [_ extension-id item-namespace server-response]]
      (let [db (r events/receive-item! db extension-id item-namespace server-response)]
           (if-let [item-label (r subs/get-item-label db extension-id item-namespace)]
                   {:db db :dispatch-if [(r subs/set-title? db extension-id item-namespace)
                                         [:ui/set-title! item-label]]}
                   {:db db}))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/update-item!
  (fn [{:keys [db]} [_ extension-id item-namespace]]))
      ;(let [query        (r queries/get-update-item-alias-query db media-item item-alias)
      ;      validator-f #()]))
           ;[:sync/send-query! :storage.media-browser/update-item!
           ;                   {:on-success [:item-lister/reload-items! :storage :media]
           ;                    :on-failure [:ui/blow-bubble! {:body :failed-to-rename}]
           ;                    :query query :validator-f validator-f



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/delete-item!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @usage
  ;  [:item-browser/delete-item! :my-extension :my-type "my-item"]
  (fn [{:keys [db]} [_ extension-id item-namespace item-id]]
      (let [query        (r queries/get-delete-item-query          db extension-id item-namespace item-id)
            validator-f #(r validators/delete-item-response-valid? db extension-id item-namespace %)]
           {:db (r events/delete-item! db extension-id item-namespace item-id)
            :dispatch [:sync/send-query! (engine/request-id extension-id item-namespace)
                                         {:on-success [:item-browser/item-deleted       extension-id item-namespace item-id]
                                          :on-failure [:item-browser/delete-item-failed extension-id item-namespace]
                                          :query query :validator-f validator-f}]})))

(a/reg-event-fx
  :item-browser/item-deleted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ; @param (map) server-response
  (fn [{:keys []} [_ extension-id item-namespace item-id _]]
      {:dispatch-n [[:item-browser/render-item-deleted-dialog! extension-id item-namespace item-id]
                    [:item-browser/reload-items!               extension-id item-namespace]]}))

(a/reg-event-fx
  :item-browser/delete-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  (fn [{:keys [db]} [_ extension-id item-namespace _]]
      ; Ha az elem törlése sikertelen volt ...
      ; ... befejezi progress-bar elemen kijelzett folyamatot
      ; ... megjelenít egy értesítést
      {:dispatch-n [[:ui/end-fake-process!]
                    [:ui/blow-bubble! {:body :failed-to-delete}]]}))

(a/reg-event-fx
  :item-browser/undo-delete-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  (fn [{:keys [db]} [_ extension-id item-namespace item-id]]
      (let [query        (r queries/get-undo-delete-item-query          db extension-id item-namespace item-id)
            validator-f #(r validators/undo-delete-item-response-valid? db extension-id item-namespace %)]
           {:db (r ui/fake-process! db 15)
            :dispatch [:sync/send-query! (engine/request-id extension-id item-namespace)
                                         {:on-success [:item-browser/delete-item-undid       extension-id item-namespace]
                                          :on-failure [:item-browser/undo-delete-item-failed extension-id item-namespace]
                                          :query query :validator-f validator-f}]})))

(a/reg-event-fx
  :item-browser/delete-item-undid
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  (fn [{:keys [db]} [_ extension-id item-namespace _]]
      [:item-browser/reload-items! extension-id item-namespace]))

(a/reg-event-fx
  :item-browser/undo-delete-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  (fn [{:keys [db]} [_ extension-id item-namespace _]]
      ; Ha az elem törlésének visszaállítása sikertelen volt ...
      ; ... befejezi progress-bar elemen kijelzett folyamatot
      ; ... megjelenít egy értesítést
      {:dispatch-n [[:ui/end-fake-process!]
                    [:ui/blow-bubble! {:body :failed-to-undo-delete}]]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/duplicate-item!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @usage
  ;  [:item-browser/duplicate-item! :my-extension :my-type "my-item"]
  (fn [{:keys [db]} [_ extension-id item-namespace item-id]]
      (let [query        (r queries/get-duplicate-item-query          db extension-id item-namespace item-id)
            validator-f #(r validators/duplicate-item-response-valid? db extension-id item-namespace %)]
           {:db (r ui/fake-process! db 15)
            :dispatch [:sync/send-query! (engine/request-id extension-id item-namespace)
                                         {:on-success [:item-browser/item-duplicated       extension-id item-namespace]
                                          :on-failure [:item-browser/duplicate-item-failed extension-id item-namespace]
                                          :query query :validator-f validator-f}]})))

(a/reg-event-fx
  :item-browser/item-duplicated
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  (fn [{:keys [db]} [_ extension-id item-namespace server-response]]
      (let [copy-id (engine/server-response->copy-id extension-id item-namespace server-response)]
           {:dispatch-n [[:item-browser/reload-items!                  extension-id item-namespace]
                         [:item-browser/render-item-duplicated-dialog! extension-id item-namespace copy-id]]})))

(a/reg-event-fx
  :item-browser/duplicate-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      ; Ha az elem duplikálása sikertelen volt ...
      ; ... befejezi progress-bar elemen kijelzett folyamatot
      ; ... megjelenít egy értesítést
      {:dispatch-n [[:ui/end-fake-process!]
                    [:ui/blow-bubble! {:body :failed-to-duplicate}]]}))

(a/reg-event-fx
  :item-browser/undo-duplicate-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings) copy-id
  (fn [{:keys [db]} [_ extension-id item-namespace copy-ids]]
      (let [query        (r queries/get-undo-duplicate-item-query          db extension-id item-namespace copy-ids)
            validator-f #(r validators/undo-duplicate-item-response-valid? db extension-id item-namespace %)]
           {:db (r ui/fake-process! db 15)
            :dispatch [:sync/send-query! (engine/request-id extension-id item-namespace)
                                         {:on-success [:item-browser/reload-items!              extension-id item-namespace]
                                          :on-failure [:item-browser/undo-duplicate-item-failed extension-id item-namespace]
                                          :query query :validator-f validator-f}]})))

(a/reg-event-fx
  :item-browser/undo-duplicate-item-failed
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
  :item-browser/move-item!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ; @param (string) destination-id
  ;
  ; @usage
  ;  [:item-browser/move-item! :my-extension :my-type "my-item" "your-item"]
  (fn [{:keys [db]} [_ extension-id item-namespace item-id destination-id]]))
