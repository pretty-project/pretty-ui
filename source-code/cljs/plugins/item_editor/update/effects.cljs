
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.update.effects
    (:require [plugins.item-editor.core.events       :as core.events]
              [plugins.item-editor.core.subs         :as core.subs]
              [plugins.item-editor.routes.subs       :as routes.subs]
              [plugins.item-editor.transfer.subs     :as transfer.subs]
              [plugins.item-editor.update.events     :as update.events]
              [plugins.item-editor.update.queries    :as update.queries]
              [plugins.item-editor.update.subs       :as update.subs]
              [plugins.item-editor.update.validators :as update.validators]
              [plugins.item-editor.update.views      :as update.views]
              [x.app-core.api                        :as a :refer [r]]
              [x.app-ui.api                          :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/render-item-deleted-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [current-item-id (r core.subs/get-current-item-id db extension-id item-namespace)]
           [:ui/blow-bubble! :plugins.item-editor/item-deleted-dialog
                             {:body       [update.views/item-deleted-dialog-body extension-id item-namespace current-item-id]
                              :destructor [:item-editor/clean-recovery-data!     extension-id item-namespace current-item-id]}])))

(a/reg-event-fx
  :item-editor/render-changes-discarded-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  (fn [{:keys [db]} [_ extension-id item-namespace item-id]]
      ; Az [:item-editor/render-changes-discarded-dialog! ...] esemény paraméterként kapja az item-editor
      ; plugin elhagyása előtt szerkesztett elem azonosítóját, mert az ... esemény megtörténésekor az azonosító
      ; már nem elérhető a Re-Frame adatbázisban.
      [:ui/blow-bubble! :plugins.item-editor/changes-discarded-dialog
                        {:body       [update.views/changes-discarded-dialog-body extension-id item-namespace item-id]
                         :destructor [:item-editor/clean-recovery-data!          extension-id item-namespace item-id]}]))

(a/reg-event-fx
  :item-editor/render-item-duplicated-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) copy-id
  (fn [_ [_ extension-id item-namespace copy-id]]
      [:ui/blow-bubble! :plugins.item-editor/item-duplicated-dialog
                        {:body [update.views/item-duplicated-dialog-body extension-id item-namespace copy-id]}]))



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
      ;   miközben az item-editor plugin továbbra is "Új elem hozzáadása" módban fut, ezért
      ;   nem tudná megkülönbözetni a további mentéseket a hozzáadástól (első mentéstől)!
      ; - Az elem esetleges törlése utáni – kliens-oldali adatból történő – visszaállításhoz
      ;   szükséges az elem feltételezett szerver-oldali állapotáról másolatot tárolni!
      ; - Az elem szerverre küldésének idejében az elemről másolat készítése feltételezi,
      ;   a mentés sikerességét. Sikertelen mentés esetén a kliens-oldali másolat eltérhet
      ;   a szerver-oldalon tárolt változattól, ami az elem törlése utáni visszaállítás esetén
      ;   pontatlan visszaálltást okozhat!
      (let [query        (r update.queries/get-save-item-query          db extension-id item-namespace)
            validator-f #(r update.validators/save-item-response-valid? db extension-id item-namespace %)]
           {:db (r update.events/save-item! db extension-id item-namespace)
            :dispatch [:sync/send-query! (r core.subs/get-request-id db extension-id item-namespace)
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
      (if-let [base-route (r transfer.subs/get-transfer-item db extension-id item-namespace :base-route)]
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
      (let [query        (r update.queries/get-delete-item-query          db extension-id item-namespace)
            validator-f #(r update.validators/delete-item-response-valid? db extension-id item-namespace %)]
           {:db (r ui/fake-process! db 15)
            :dispatch [:sync/send-query! (r core.subs/get-request-id db extension-id item-namespace)
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
      {:db (r update.events/item-deleted db extension-id item-namespace)
       :dispatch-n [[:item-editor/render-item-deleted-dialog! extension-id item-namespace]
                    (if-let [base-route (r transfer.subs/get-transfer-item db extension-id item-namespace :base-route)]
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
      (let [query        (r update.queries/get-undo-delete-item-query          db extension-id item-namespace item-id)
            validator-f #(r update.validators/undo-delete-item-response-valid? db extension-id item-namespace %)]
           {:db (r ui/fake-process! db 15)
            :dispatch [:sync/send-query! (r core.subs/get-request-id db extension-id item-namespace)
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
      {:db (r core.events/set-recovery-mode! db extension-id item-namespace)
       :dispatch-n [(if-let [item-route (r routes.subs/get-item-route db extension-id item-namespace item-id)]
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
      (let [query        (r update.queries/get-duplicate-item-query          db extension-id item-namespace)
            validator-f #(r update.validators/duplicate-item-response-valid? db extension-id item-namespace %)]
           [:sync/send-query! (r core.subs/get-request-id db extension-id item-namespace)
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
      (let [copy-id (r update.subs/get-copy-id db extension-id item-namespace server-response)]
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
      {:db (r core.events/set-recovery-mode! db extension-id item-namespace)
       :dispatch [:item-editor/edit-item! extension-id item-namespace item-id]}))
