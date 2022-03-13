
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.update.effects
    (:require [plugins.item-lister.core.subs         :as core.subs]
              [plugins.item-lister.update.queries    :as update.queries]
              [plugins.item-lister.update.validators :as update.validators]
              [plugins.item-lister.update.views      :as update.views]
              [x.app-core.api                        :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/render-items-deleted-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  (fn [_ [_ extension-id item-namespace item-ids]]
      [:ui/blow-bubble! :plugins.item-lister/items-deleted-dialog
                        {:body       [update.views/items-deleted-dialog-body extension-id item-namespace item-ids]
                         :destructor [:item-lister/clean-backup-items!       extension-id item-namespace item-ids]}]))

(a/reg-event-fx
  :item-lister/render-items-duplicated-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) copy-ids
  (fn [_ [_ extension-id item-namespace copy-ids]]
      [:ui/blow-bubble! :plugins.item-lister/items-duplicated-dialog
                        {:body [update.views/items-duplicated-dialog-body extension-id item-namespace copy-ids]}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/delete-selected-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [item-ids     (r selection.subs/get-selected-item-ids           db extension-id item-namespace)
            request-id   (r core.subs/get-request-id                       db extension-id item-namespace)
            query        (r update.queries/get-delete-items-query          db extension-id item-namespace item-ids)
            validator-f #(r update.validators/delete-items-response-valid? db extension-id item-namespace %)]
           {:db (r update.vents/delete-selected-items! db extension-id item-namespace)
            :dispatch [:sync/send-query! request-id
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
      (let [item-ids (r update.subs/get-deleted-item-ids db extension-id item-namespace server-response)]
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
      {:db (r update.events/delete-items-failed db extension-id item-namespace)
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
      (let [request-id   (r core.subs/get-request-id                            db extension-id item-namespace)
            query        (r update.queries/get-undo-delete-items-query          db extension-id item-namespace item-ids)
            validator-f #(r update.validators/undo-delete-items-response-valid? db extension-id item-namespace %)]
           {:db (r ui/fake-process! db 15)
            :dispatch [:sync/send-query! request-id
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
      (let [item-ids     (r selection.subs/get-selected-item-ids              db extension-id item-namespace)
            request-id   (r core.subs/get-request-id                          db extension-id item-namespace)
            query        (r update.queries/get-duplicate-items-query          db extension-id item-namespace item-ids)
            validator-f #(r update.validators/duplicate-items-response-valid? db extension-id item-namespace %)]
           {:db (r ui/fake-process! db 15)
            :dispatch [:sync/send-query! request-id
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
      (let [copy-ids (r update.subs/get-duplicated-item-ids db extension-id item-namespace server-response)]
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
      (let [request-id   (r core.subs/get-request-id                               db extension-id item-namespace)
            query        (r update.queries/get-undo-duplicate-items-query          db extension-id item-namespace copy-ids)
            validator-f #(r update.validators/undo-duplicate-items-response-valid? db extension-id item-namespace %)]
           {:db (r ui/fake-process! db 15)
            :dispatch [:sync/send-query! request-id
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
