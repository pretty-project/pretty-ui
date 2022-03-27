
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.update.effects
    (:require [plugins.item-browser.core.subs         :as core.subs]
              [plugins.item-browser.update.events     :as update.events]
              [plugins.item-browser.update.queries    :as update.queries]
              [plugins.item-browser.update.subs       :as update.subs]
              [plugins.item-browser.update.validators :as update.validators]
              [plugins.item-browser.update.views      :as update.views]
              [x.app-core.api                         :as a :refer [r]]
              [x.app-ui.api                           :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/render-item-deleted-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  (fn [_ [_ browser-id item-id]]
      [:ui/blow-bubble! :plugins.item-browser/item-deleted-dialog
                        {:body [update.views/item-deleted-dialog-body browser-id item-id]}]))
                         ;:destructor [:item-browser/clean-backup-items! browser-id item-id]}]))

(a/reg-event-fx
  :item-browser/render-item-duplicated-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) copy-id
  (fn [_ [_ browser-id copy-id]]
      [:ui/blow-bubble! :plugins.item-browser/item-duplicated-dialog
                        {:body [update.views/item-duplicated-dialog-body browser-id copy-id]}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/update-item!
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ; @param (map) changes
  ;
  ; @usage
  ;  [:item-browser/update-item! :my-browser "my-item" {...}]
  (fn [{:keys [db]} [_ browser-id item-id changes]]
      ; - Az [:item-browser/update-item! ...] esemény a changes paraméterként átadott változásokat
      ;   azonnal végrahajta az elemen
      ; - Ha az elem szerver-oldali változatának felülírása sikertelen volt, akkor a kliens-oldali
      ;   változat a tárolt biztonsági mentésből helyreállítódik
      ; - Egy időben egy változtatást lehetséges az elemen végrehajtani, mert egy darab biztonsági
      ;   mentéssel nem lehetséges az időben átfedésbe kerülő változtatásokat kezelni, ezért a szerver
      ;   válaszának megérkezéséig az elem {:disabled? true} állapotban van
      (let [db           (r update.events/update-item!                    db browser-id item-id changes)
            query        (r update.queries/get-update-item-query          db browser-id item-id)
            validator-f #(r update.validators/update-item-response-valid? db browser-id %)]
           {:db db :dispatch [:sync/send-query! :storage.media-browser/update-item!
                                                {:display-progress? true
                                                 :on-success [:item-browser/item-updated       browser-id item-id]
                                                 :on-failure [:item-browser/update-item-failed browser-id item-id]
                                                 :query query :validator-f validator-f}]})))

(a/reg-event-fx
  :item-browser/item-updated
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ browser-id item-id _]]
      {:db (r update.events/item-updated db browser-id item-id)}))

(a/reg-event-fx
  :item-browser/update-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ browser-id item-id _]]
      {:db (r update.events/update-item-failed db browser-id item-id)
       :dispatch [:ui/blow-bubble! {:body :failed-to-update}]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/delete-item!
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  [:item-browser/delete-item! :my-browser "my-item"]
  (fn [{:keys [db]} [_ browser-id item-id]]
      (let [query        (r update.queries/get-delete-item-query          db browser-id item-id)
            validator-f #(r update.validators/delete-item-response-valid? db browser-id %)]
           {:db (r update.events/delete-item! db browser-id item-id)
            :dispatch [:sync/send-query! (r core.subs/get-request-id db browser-id)
                                         {:on-success [:item-browser/item-deleted       browser-id item-id]
                                          :on-failure [:item-browser/delete-item-failed browser-id item-id]
                                          :query query :validator-f validator-f}]})))

(a/reg-event-fx
  :item-browser/item-deleted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ; @param (map) server-response
  (fn [{:keys []} [_ browser-id item-id _]]
      {:dispatch-n [[:item-browser/render-item-deleted-dialog! browser-id item-id]
                    [:item-browser/reload-items!               browser-id]]}))

(a/reg-event-fx
  :item-browser/delete-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ browser-id item-id _]]
      ; Ha az elem törlése sikertelen volt ...
      ; ... engedélyezi az ideiglenesen letiltott elemet
      ; ... befejezi progress-bar elemen kijelzett folyamatot
      ; ... megjelenít egy értesítést
      {:db (r update.events/delete-item-failed db browser-id item-id)
       :dispatch-n [[:ui/end-fake-process!]
                    [:ui/blow-bubble! {:body :failed-to-delete}]]}))

(a/reg-event-fx
  :item-browser/undo-delete-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (string) item-id
  (fn [{:keys [db]} [_ browser-id item-id]]
      (let [query        (r update.queries/get-undo-delete-item-query          db browser-id item-id)
            validator-f #(r update.validators/undo-delete-item-response-valid? db browser-id %)]
           {:db (r ui/fake-process! db 15)
            :dispatch [:sync/send-query! (r core.subs/get-request-id db browser-id)
                                         {:on-success [:item-browser/delete-item-undid       browser-id]
                                          :on-failure [:item-browser/undo-delete-item-failed browser-id]
                                          :query query :validator-f validator-f}]})))

(a/reg-event-fx
  :item-browser/delete-item-undid
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ browser-id _]]
      [:item-browser/reload-items! browser-id]))

(a/reg-event-fx
  :item-browser/undo-delete-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ browser-id _]]
      ; Ha az elem törlésének visszaállítása sikertelen volt ...
      ; ... befejezi progress-bar elemen kijelzett folyamatot
      ; ... megjelenít egy értesítést
      {:dispatch-n [[:ui/end-fake-process!]
                    [:ui/blow-bubble! {:body :failed-to-undo-delete}]]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/duplicate-item!
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  [:item-browser/duplicate-item! :my-browser "my-item"]
  (fn [{:keys [db]} [_ browser-id item-id]]
      (let [query        (r update.queries/get-duplicate-item-query          db browser-id item-id)
            validator-f #(r update.validators/duplicate-item-response-valid? db browser-id %)]
           {:db (r ui/fake-process! db 15)
            :dispatch [:sync/send-query! (r core.subs/get-request-id db browser-id)
                                         {:on-success [:item-browser/item-duplicated       browser-id]
                                          :on-failure [:item-browser/duplicate-item-failed browser-id]
                                          :query query :validator-f validator-f}]})))

(a/reg-event-fx
  :item-browser/item-duplicated
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ browser-id server-response]]
      (let [copy-id (r update.subs/get-copy-id db browser-id server-response)]
           {:dispatch-n [[:item-browser/reload-items!                  browser-id]
                         [:item-browser/render-item-duplicated-dialog! browser-id copy-id]]})))

(a/reg-event-fx
  :item-browser/duplicate-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  (fn [{:keys [db]} [_ browser-id]]
      ; Ha az elem duplikálása sikertelen volt ...
      ; ... befejezi progress-bar elemen kijelzett folyamatot
      ; ... megjelenít egy értesítést
      {:dispatch-n [[:ui/end-fake-process!]
                    [:ui/blow-bubble! {:body :failed-to-duplicate}]]}))

(a/reg-event-fx
  :item-browser/undo-duplicate-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (strings) copy-id
  (fn [{:keys [db]} [_ browser-id copy-id]]
      (let [query        (r update.queries/get-undo-duplicate-item-query          db browser-id copy-id)
            validator-f #(r update.validators/undo-duplicate-item-response-valid? db browser-id %)]
           {:db (r ui/fake-process! db 15)
            :dispatch [:sync/send-query! (r core.subs/get-request-id db browser-id)
                                         {:on-success [:item-browser/reload-items!              browser-id]
                                          :on-failure [:item-browser/undo-duplicate-item-failed browser-id]
                                          :query query :validator-f validator-f}]})))

(a/reg-event-fx
  :item-browser/undo-duplicate-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ browser-id _]]
      ; Ha a kijelölt elemek duplikálásának visszavonása sikertelen volt ...
      ; ... befejezi progress-bar elemen kijelzett folyamatot
      ; ... megjelenít egy értesítést
      {:dispatch-n [[:ui/end-fake-process!]
                    [:ui/blow-bubble! {:body :failed-to-undo-duplicate}]]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/move-item!
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ; @param (string) destination-id
  ;
  ; @usage
  ;  [:item-browser/move-item! :my-browser "my-item" "your-item"]
  (fn [{:keys [db]} [_ browser-id item-id destination-id]]))
