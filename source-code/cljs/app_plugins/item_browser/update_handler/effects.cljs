
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-browser.update-handler.effects
    (:require [app-plugins.item-browser.update-handler.events :as update-handler.events]
              [app-plugins.item-browser.update-handler.subs   :as update-handler.subs]
              [app-plugins.item-browser.update-handler.views  :as update-handler.views]
              [x.app-core.api                                 :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/render-item-deleted-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  (fn [_ [_ extension-id item-namespace item-id]]
      [:ui/blow-bubble! :plugins.item-browser/item-deleted-dialog
                        {:body [update-handler.views/item-deleted-dialog-body extension-id item-namespace item-id]}]))
                         ;:destructor [:item-browser/clean-backup-items! extension-id item-namespace item-id]}]))

(a/reg-event-fx
  :item-browser/render-item-duplicated-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) copy-id
  (fn [_ [_ extension-id item-namespace copy-id]]
      [:ui/blow-bubble! :plugins.item-browser/item-duplicated-dialog
                        {:body [update-handler.views/item-duplicated-dialog-body extension-id item-namespace copy-id]}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/update-item!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ; @param (map) changes
  ;
  ; @usage
  ;  [:item-browser/update-item! :my-extension :my-type "my-item" {...}]
  (fn [{:keys [db]} [_ extension-id item-namespace item-id changes]]
      ; - Az [:item-browser/update-item! ...] esemény a changes paraméterként átadott változásokat
      ;   azonnal végrahajta az elemen
      ; - Ha az elem szerver-oldali változatának felülírása sikertelen volt, akkor a kliens-oldali
      ;   változat a tárolt biztonsági mentésből helyreállítódik
      ; - Egy időben egy változtatást lehetséges az elemen végrehajtani, mert egy darab biztonsági
      ;   mentéssel nem lehetséges az időben átfedésbe kerülő változtatásokat kezelni, ezért a szerver
      ;   válaszának megérkezéséig az elem {:disabled? true} állapotban van
      (let [db           (r update-handler.events/update-item!                    db extension-id item-namespace item-id changes)
            query        (r update-handler.queries/get-update-item-query          db extension-id item-namespace item-id)
            validator-f #(r update-handler.validators/update-item-response-valid? db extension-id item-namespace %)]
           {:db db :dispatch [:sync/send-query! :storage.media-browser/update-item!
                                                {:display-progress? true
                                                 :on-success [:item-browser/item-updated       extension-id item-namespace item-id]
                                                 :on-failure [:item-browser/update-item-failed extension-id item-namespace item-id]
                                                 :query query :validator-f validator-f}]})))

(a/reg-event-fx
  :item-browser/item-updated
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ extension-id item-namespace item-id _]]
      {:db (r update-handler.events/item-updated db extension-id item-namespace item-id)}))

(a/reg-event-fx
  :item-browser/update-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ extension-id item-namespace item-id _]]
      {:db (r update-handler.events/update-item-failed db extension-id item-namespace item-id)
       :dispatch [:ui/blow-bubble! {:body :failed-to-update}]}))



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
      (let [request-id   (r browser-handler.subs/get-request-id                   db extension-id item-namespace)
            query        (r update-handler.queries/get-delete-item-query          db extension-id item-namespace item-id)
            validator-f #(r update-handler.validators/delete-item-response-valid? db extension-id item-namespace %)]
           {:db (r events/delete-item! db extension-id item-namespace item-id)
            :dispatch [:sync/send-query! request-id
                                         {:on-success [:item-browser/item-deleted       extension-id item-namespace item-id]
                                          :on-failure [:item-browser/delete-item-failed extension-id item-namespace item-id]
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
  ; @param (string) item-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ extension-id item-namespace item-id _]]
      ; Ha az elem törlése sikertelen volt ...
      ; ... engedélyezi az ideiglenesen letiltott elemet
      ; ... befejezi progress-bar elemen kijelzett folyamatot
      ; ... megjelenít egy értesítést
      {:db (r update-handler.events/delete-item-failed db extension-id item-namespace item-id)
       :dispatch-n [[:ui/end-fake-process!]
                    [:ui/blow-bubble! {:body :failed-to-delete}]]}))

(a/reg-event-fx
  :item-browser/undo-delete-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  (fn [{:keys [db]} [_ extension-id item-namespace item-id]]
      (let [request-id   (r browser-handler.subs/get-request-id                        db extension-id item-namespace)
            query        (r update-handler.queries/get-undo-delete-item-query          db extension-id item-namespace item-id)
            validator-f #(r update-handler.validators/undo-delete-item-response-valid? db extension-id item-namespace %)]
           {:db (r ui/fake-process! db 15)
            :dispatch [:sync/send-query! request-id
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
      (let [request-id   (r browser-handler.subs/get-request-id                      db extension-id item-namespace)
            query        (r update-handler.queries/get-duplicate-item-query          db extension-id item-namespace item-id)
            validator-f #(r update-handler.validators/duplicate-item-response-valid? db extension-id item-namespace %)]
           {:db (r ui/fake-process! db 15)
            :dispatch [:sync/send-query! request-id
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
      (let [copy-id (r update-handler.subs/get-copy-id db extension-id item-namespace server-response)]
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
  (fn [{:keys [db]} [_ extension-id item-namespace copy-id]]
      (let [request-id   (r browser-handler.subs/get-request-id                           db extension-id item-namespace)
            query        (r update-handler.queries/get-undo-duplicate-item-query          db extension-id item-namespace copy-id)
            validator-f #(r update-handler.validators/undo-duplicate-item-response-valid? db extension-id item-namespace %)]
           {:db (r ui/fake-process! db 15)
            :dispatch [:sync/send-query! request-id
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
