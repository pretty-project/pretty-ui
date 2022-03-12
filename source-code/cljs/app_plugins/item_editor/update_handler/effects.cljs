
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-editor.update-handler.effects
    (:require [app-plugins.item-editor.editor-handler.subs       :as editor-handler.subs]
              [app-plugins.item-editor.update-handler.events     :as update-handler.events]
              [app-plugins.item-editor.update-handler.queries    :as update-handler.queries]
              [app-plugins.item-editor.update-handler.subs       :as update-handler.subs]
              [app-plugins.item-editor.update-handler.validators :as update-handler.validators]
              [x.app-core.api                                    :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/render-item-deleted-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [current-item-id (r editor-handler.subs/get-current-item-id db extension-id item-namespace)]
           [:ui/blow-bubble! :plugins.item-editor/item-deleted-dialog
                             {:body       [item-handler.views/item-deleted-dialog-body extension-id item-namespace current-item-id]
                              :destructor [:item-editor/clean-recovery-data!           extension-id item-namespace current-item-id]}])))

(a/reg-event-fx
  :item-editor/render-changes-discarded-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [current-item-id (r editor-handler.subs/get-current-item-id db extension-id item-namespace)]
           [:ui/blow-bubble! :plugins.item-editor/changes-discarded-dialog
                             {:body       [item-handler.views/changes-discarded-dialog-body extension-id item-namespace current-item-id]
                              :destructor [:item-editor/clean-recovery-data!                extension-id item-namespace current-item-id]}])))

(a/reg-event-fx
  :item-editor/render-item-duplicated-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) copy-id
  (fn [_ [_ extension-id item-namespace copy-id]]
      [:ui/blow-bubble! :plugins.item-editor/item-duplicated-dialog
                        {:body [update-handler.views/item-duplicated-dialog-body extension-id item-namespace copy-id]}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/save-item!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-editor/save-item! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      ; - Az új elemek hozzáadása (mentése), azért nem különálló [:item-editor/add-item! ...] eseménnyel
      ;   történik, mert az új elem szerver-oldali hozzáadása (kliens-oldali első mentése) utáni,
      ;   az aktuális szerkesztés közbeni további mentések, már nem számítának elem-hozzáadásnak,
      ;   miközben az item-editor plugin továbbra is "új elem hozzáadása" módban fut, ezért
      ;   nem tudná megkülönbözetni a további mentéseket a hozzáadástól (első mentés)!
      ; - Az elem esetleges törlése utáni – kliens-oldali adatból történő – visszaállításhoz
      ;   szükséges az elem feltételezett szerver-oldali állapotáról másolatot tárolni!
      ; - Az elem szerverre küldésének idejében az elemről másolat készítése feltételezi,
      ;   a mentés sikerességét. Sikertelen mentés esetén a kliens-oldali másolat eltérhet
      ;   a szerver-oldalon tárolt változattól, ami az elem törlése utáni visszaállítás esetén
      ;   pontatlan visszaálltást okozhat!
      (let [request-id   (r editor-handler.subs/get-request-id                  db extension-id item-namespace)
            query        (r update-handler.queries/get-save-item-query          db extension-id item-namespace)
            validator-f #(r update-handler.validators/save-item-response-valid? db extension-id item-namespace %)]
           {:db (r events/save-item! db extension-id item-namespace)
            :dispatch [:sync/send-query! request-id
                                         {:on-success [:item-editor/item-saved       extension-id item-namespace]
                                          :on-failure [:item-editor/save-item-failed extension-id item-namespace]
                                          :query query :validator-f validator-f}]})))

(a/reg-event-fx
  :item-editor/item-saved
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  (fn [{:keys [db]} [_ extension-id item-namespace _]]
      (if-let [base-route (r editor-handler.subs/get-meta-item db extension-id item-namespace :base-route)]
              [:router/go-to! base-route]
              [:ui/end-fake-process!])))

(a/reg-event-fx
  :item-editor/save-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  (fn [{:keys [db]} [_ extension-id item-namespace _]]
      ; Ha az elem mentése sikertelen volt ...
      ; ... befejezi progress-bar elemen kijelzett folyamatot
      ; ... megjelenít egy értesítést
      {:dispatch-n [[:ui/end-fake-process!]
                    [:ui/blow-bubble! {:body :failed-to-save}]]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/delete-item!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-editor/delete-item! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [request-id   (r editor-handler.subs/get-request-id                    db extension-id item-namespace)
            query        (r update-handler.queries/get-delete-item-query          db extension-id item-namespace)
            validator-f #(r update-handler.validators/delete-item-response-valid? db extension-id item-namespace %)]
           {:db (r ui/fake-process! db 15)
            :dispatch [:sync/send-query! request-id
                                         {:on-success [:item-editor/item-deleted       extension-id item-namespace]
                                          :on-failure [:item-editor/delete-item-failed extension-id item-namespace]
                                          :query query :validator-f validator-f}]})))

(a/reg-event-fx
  :item-editor/item-deleted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      {:db (r update-handler.events/item-deleted db extension-id item-namespace)
       :dispatch-n [[:item-editor/render-item-deleted-dialog! extension-id item-namespace]
                    (if-let [base-route (r editor-handler.subs/get-meta-item db extension-id item-namespace :base-route)]
                            [:router/go-to! base-route]
                            [:ui/end-fake-process!])]}))

(a/reg-event-fx
  :item-editor/delete-item-failed
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
  :item-editor/undo-delete-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  (fn [{:keys [db]} [_ extension-id item-namespace item-id]]
      (let [request-id   (r editor-handler.subs/get-request-id                         db extension-id item-namespace)
            query        (r update-handler.queries/get-undo-delete-item-query          db extension-id item-namespace item-id)
            validator-f #(r update-handler.validators/undo-delete-item-response-valid? db extension-id item-namespace %)]
           {:db (r ui/fake-process! db 15)
            :dispatch [:sync/send-query! request-id
                                         {:on-success [:item-editor/delete-item-undid       extension-id item-namespace item-id]
                                          :on-failure [:item-editor/undo-delete-item-failed extension-id item-namespace]
                                          :query query :validator-f validator-f}]})))

(a/reg-event-fx
  :item-editor/delete-item-undid
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ extension-id item-namespace item-id _]]
      {:db (r editor-handler.events/set-recovery-mode! db extension-id item-namespace)
       :dispatch-n [(if-let [item-route (r route-handler.subs/get-item-route db extension-id item-namespace item-id)]
                            [:router/go-to! item-route]
                            [:ui/end-fake-process!])]}))

(a/reg-event-fx
  :item-editor/undo-delete-item-failed
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
  :item-editor/duplicate-item!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-editor/duplicate-item! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [request-id   (r editor-handler.subs/get-request-id                       db extension-id item-namespace)
            query        (r update-handler.queries/get-duplicate-item-query          db extension-id item-namespace)
            validator-f #(r update-handler.validators/duplicate-item-response-valid? db extension-id item-namespace %)]
           [:sync/send-query! request-id
                              {:display-progress? true
                               :on-success [:item-editor/item-duplicated       extension-id item-namespace]
                               :on-failure [:item-editor/duplicate-item-failed extension-id item-namespace]
                               :query query :validator-f validator-f}])))

(a/reg-event-fx
  :item-editor/item-duplicated
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  (fn [{:keys [db]} [_ extension-id item-namespace server-response]]
      (let [copy-id (r update-handler.subs/get-copy-id db extension-id item-namespace server-response)]
           [:item-editor/render-item-duplicated-dialog! extension-id item-namespace copy-id])))

(a/reg-event-fx
  :item-editor/duplicate-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  (fn [{:keys [db]} [_ extension-id item-namespace _]]
      ; Ha az elem duplikálása sikertelen volt ...
      ; ... megjelenít egy értesítést
      [:ui/blow-bubble! {:body :failed-to-duplicate}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/undo-discard-changes!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  (fn [{:keys [db]} [_ extension-id item-namespace item-id]]
      {:db (r editor-handler.events/set-recovery-mode! db extension-id item-namespace)
       :dispatch [:item-editor/edit-item! extension-id item-namespace item-id]}))
