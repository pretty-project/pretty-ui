
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
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ editor-id]]
      (let [current-item-id (r core.subs/get-current-item-id db editor-id)]
           [:ui/blow-bubble! :plugins.item-editor/item-deleted-dialog
                             {:body       [update.views/item-deleted-dialog-body editor-id current-item-id]
                              :destructor [:item-editor/clean-recovery-data!     editor-id current-item-id]}])))

(a/reg-event-fx
  :item-editor/render-changes-discarded-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (string) item-id
  (fn [{:keys [db]} [_ editor-id item-id]]
      ; Az [:item-editor/render-changes-discarded-dialog! ...] esemény paraméterként kapja az item-editor
      ; plugin elhagyása előtt szerkesztett elem azonosítóját, mert az ... esemény megtörténésekor az azonosító
      ; már nem elérhető a Re-Frame adatbázisban.
      [:ui/blow-bubble! :plugins.item-editor/changes-discarded-dialog
                        {:body       [update.views/changes-discarded-dialog-body editor-id item-id]
                         :destructor [:item-editor/clean-recovery-data!          editor-id item-id]}]))

(a/reg-event-fx
  :item-editor/render-item-duplicated-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (string) copy-id
  (fn [_ [_ editor-id copy-id]]
      [:ui/blow-bubble! :plugins.item-editor/item-duplicated-dialog
                        {:body [update.views/item-duplicated-dialog-body editor-id copy-id]}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/save-item!
  ; @param (keyword) editor-id
  ;
  ; @usage
  ;  [:item-editor/save-item! :my-editor]
  (fn [{:keys [db]} [_ editor-id]]
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
      (let [query        (r update.queries/get-save-item-query          db editor-id)
            validator-f #(r update.validators/save-item-response-valid? db editor-id %)]
           {:db (r update.events/save-item! db editor-id)
            :dispatch [:sync/send-query! (r core.subs/get-request-id db editor-id)
                                         {:on-success [:item-editor/item-saved       editor-id]
                                          :on-failure [:item-editor/save-item-failed editor-id]
                                          :query query :validator-f validator-f}]})))

(a/reg-event-fx
  :item-editor/item-saved
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ editor-id _]]
      (if-let [base-route (r transfer.subs/get-transfer-item db editor-id :base-route)]
              [:router/go-to! base-route]
              [:ui/end-fake-process!])))

(a/reg-event-fx
  :item-editor/save-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ editor-id _]]
      ; Ha az elem mentése sikertelen volt ...
      ; ... befejezi progress-bar elemen kijelzett folyamatot
      ; ... megjelenít egy értesítést
      {:dispatch-n [[:ui/end-fake-process!]
                    [:ui/blow-bubble! {:body :failed-to-save}]]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/delete-item!
  ; @param (keyword) editor-id
  ;
  ; @usage
  ;  [:item-editor/delete-item! :my-editor]
  (fn [{:keys [db]} [_ editor-id]]
      (let [query        (r update.queries/get-delete-item-query          db editor-id)
            validator-f #(r update.validators/delete-item-response-valid? db editor-id %)]
           {:db (r ui/fake-process! db 15)
            :dispatch [:sync/send-query! (r core.subs/get-request-id db editor-id)
                                         {:on-success [:item-editor/item-deleted       editor-id]
                                          :on-failure [:item-editor/delete-item-failed editor-id]
                                          :query query :validator-f validator-f}]})))

(a/reg-event-fx
  :item-editor/item-deleted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ editor-id]]
      {:db (r update.events/item-deleted db editor-id)
       :dispatch-n [[:item-editor/render-item-deleted-dialog! editor-id]
                    (if-let [base-route (r transfer.subs/get-transfer-item db editor-id :base-route)]
                            [:router/go-to! base-route]
                            [:ui/end-fake-process!])]}))

(a/reg-event-fx
  :item-editor/delete-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ editor-id _]]
      ; Ha az elem törlése sikertelen volt ...
      ; ... befejezi progress-bar elemen kijelzett folyamatot
      ; ... megjelenít egy értesítést
      {:dispatch-n [[:ui/end-fake-process!]
                    [:ui/blow-bubble! {:body :failed-to-delete}]]}))

(a/reg-event-fx
  :item-editor/undo-delete-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (string) item-id
  (fn [{:keys [db]} [_ editor-id item-id]]
      (let [query        (r update.queries/get-undo-delete-item-query          db editor-id item-id)
            validator-f #(r update.validators/undo-delete-item-response-valid? db editor-id %)]
           {:db (r ui/fake-process! db 15)
            :dispatch [:sync/send-query! (r core.subs/get-request-id db editor-id)
                                         {:on-success [:item-editor/delete-item-undid       editor-id item-id]
                                          :on-failure [:item-editor/undo-delete-item-failed editor-id]
                                          :query query :validator-f validator-f}]})))

(a/reg-event-fx
  :item-editor/delete-item-undid
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (string) item-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ editor-id item-id _]]
      {:db (r core.events/set-recovery-mode! db editor-id)
       :dispatch-n [(if-let [item-route (r routes.subs/get-item-route db editor-id item-id)]
                            [:router/go-to! item-route]
                            [:ui/end-fake-process!])]}))

(a/reg-event-fx
  :item-editor/undo-delete-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ editor-id _]]
      ; Ha az elem törlésének visszaállítása sikertelen volt ...
      ; ... befejezi progress-bar elemen kijelzett folyamatot
      ; ... megjelenít egy értesítést
      {:dispatch-n [[:ui/end-fake-process!]
                    [:ui/blow-bubble! {:body :failed-to-undo-delete}]]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/duplicate-item!
  ; @param (keyword) editor-id
  ;
  ; @usage
  ;  [:item-editor/duplicate-item! :my-editor]
  (fn [{:keys [db]} [_ editor-id]]
      (let [query        (r update.queries/get-duplicate-item-query          db editor-id)
            validator-f #(r update.validators/duplicate-item-response-valid? db editor-id %)]
           [:sync/send-query! (r core.subs/get-request-id db editor-id)
                              {:display-progress? true
                               :on-success [:item-editor/item-duplicated       editor-id]
                               :on-failure [:item-editor/duplicate-item-failed editor-id]
                               :query query :validator-f validator-f}])))

(a/reg-event-fx
  :item-editor/item-duplicated
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ editor-id server-response]]
      (let [copy-id (r update.subs/get-copy-id db editor-id server-response)]
           [:item-editor/render-item-duplicated-dialog! editor-id copy-id])))

(a/reg-event-fx
  :item-editor/duplicate-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ editor-id _]]
      ; Ha az elem duplikálása sikertelen volt ...
      ; ... megjelenít egy értesítést
      [:ui/blow-bubble! {:body :failed-to-duplicate}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/undo-discard-changes!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (string) item-id
  (fn [{:keys [db]} [_ editor-id item-id]]
      {:db (r core.events/set-recovery-mode! db editor-id)
       :dispatch [:item-editor/edit-item! editor-id item-id]}))
