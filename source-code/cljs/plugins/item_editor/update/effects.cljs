
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.update.effects
    (:require [plugins.item-editor.core.events       :as core.events]
              [plugins.item-editor.core.subs         :as core.subs]
              [plugins.item-editor.mount.subs        :as mount.subs]
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
      ;   az aktuális szerkesztés közbeni további mentések, már nem számítanának elem-hozzáadásnak,
      ;   miközben az item-editor plugin továbbra is "Új elem hozzáadása" módban fut, ezért
      ;   nem tudná megkülönböztetni a további mentéseket a hozzáadástól (első mentéstől)!
      ;
      ; - Az elem esetleges törlése utáni – kliens-oldali adatból történő – visszaállításhoz
      ;   szükséges az elem feltételezett szerver-oldali állapotáról másolatot tárolni!
      ;
      ; - Az elem szerverre küldésének idejében az elemről másolat készítése feltételezi,
      ;   a mentés sikerességét. Sikertelen mentés esetén a kliens-oldali másolat eltérhet
      ;   a szerver-oldalon tárolt változattól, ami az elem törlése utáni visszaállítás esetén
      ;   pontatlan visszaálltást okozhat!
      (let [query        (r update.queries/get-save-item-query          db editor-id)
            validator-f #(r update.validators/save-item-response-valid? db editor-id %)]
           {:db       (r update.events/save-item! db editor-id)
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
  (fn [{:keys [db]} [_ editor-id server-response]]
      ; A) Ha az "Elem mentése" művelet sikeres befejeződésekor a body komponens
      ;    a React-fába van csatolva, a mentett elem van megnyitva szerkesztésre
      ;    vagy új elem hozzáadása történik és a plugin rendelkezik a {:base-route "..."}
      ;    tulajdonsággal ...
      ;    ... átirányít a {:base-route "..."} tulajdonságként a kliens-oldali kezelő számára
      ;        elküldött útvonalra.
      ;    ... feltételezi, hogy az útvonal használatakor befejeződik a progress-bar elemen
      ;        15%-ig szimulált folyamat.
      ;
      ; B) Ha az A) kimenetel feltételei nem teljesülnek ...
      ;    ... befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      (let [item-id    (r update.subs/get-saved-item-id   db editor-id server-response)
            base-route (r transfer.subs/get-transfer-item db editor-id :base-route)]
           (if (and base-route (or (r core.subs/editing-item? db editor-id item-id)
                                   (r core.subs/new-item?     db editor-id)))
               ; A)
               [:router/go-to! base-route]
               ; B)
               [:ui/end-fake-process!]))))

(a/reg-event-fx
  :item-editor/save-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ editor-id _]]
      ; A) Ha az "Elem mentése" művelet sikertelen befejeződésekor a body komponens
      ;    a React-fába van csatolva, ...
      ;    ... befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      ;    ... megjelenít egy értesítést.
      ;
      ; B) Ha az "Elem mentése" művelet sikertelen befejeződésekor a body komponens
      ;    NINCS a React-fába csatolva, ...
      ;    ... megjelenít egy értesítést.
      ;    ... feltételezi, hogy a progress-bar elemen 15%-ig szimulált folyamat befejeződött.
      (if (r mount.subs/body-did-mount? db editor-id)
          ; A)
          {:dispatch-n [[:ui/end-fake-process!]
                        [:ui/blow-bubble! {:body :failed-to-save}]]}
          ; B)
          [:ui/blow-bubble! {:body :failed-to-save}])))



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
           {:db       (r ui/fake-process! db 15)
            :dispatch [:sync/send-query! (r core.subs/get-request-id db editor-id)
                                         {:on-success [:item-editor/item-deleted       editor-id]
                                          :on-failure [:item-editor/delete-item-failed editor-id]
                                          :query query :validator-f validator-f}]})))

(a/reg-event-fx
  :item-editor/item-deleted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ editor-id server-response]]
      ; A) Ha az "Elem törlése" művelet sikeres befejeződésekor a body komponens
      ;    a React-fába van csatolva, a törölt elem van megnyitva szerkesztésre
      ;    és a plugin rendelkezik a {:base-route "..."} tulajdonsággal ...
      ;    ... átirányít a {:base-route "..."} tulajdonságként a kliens-oldali kezelő számára
      ;        elküldött útvonalra.
      ;    ... feltételezi, hogy az útvonal használatakor befejeződik a progress-bar elemen
      ;        15%-ig szimulált folyamat.
      ;
      ; B) Ha az A) kimenetel feltételei nem teljesülnek ...
      ;    ... befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      {:db         (r update.events/item-deleted db editor-id)
       :dispatch-n [[:item-editor/render-item-deleted-dialog! editor-id]
                    (let [item-id    (r update.subs/get-deleted-item-id   db editor-id server-response)
                          base-route (r transfer.subs/get-transfer-item db editor-id :base-route)]
                         (if (and base-route (r core.subs/editing-item? db editor-id item-id))
                             ; A)
                             [:router/go-to! base-route]
                             ; B)
                             [:ui/end-fake-process!]))]}))

(a/reg-event-fx
  :item-editor/delete-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ editor-id _]]
      ; A) Ha az "Elem törlése" művelet sikertelen befejeződésekor a body komponens
      ;    a React-fába van csatolva, ...
      ;    ... befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      ;    ... megjelenít egy értesítést.
      ;
      ; B) Ha az "Elem törlése" művelet sikertelen befejeződésekor a body komponens
      ;    NINCS a React-fába csatolva, ...
      ;    ... megjelenít egy értesítést.
      ;    ... feltételezi, hogy a progress-bar elemen 15%-ig szimulált folyamat befejeződött.
      (if (r mount.subs/body-did-mount? db editor-id)
          ; A)
          {:dispatch-n [[:ui/end-fake-process!]
                        [:ui/blow-bubble! {:body :failed-to-delete}]]}
          ; B)
          [:ui/blow-bubble! {:body :failed-to-delete}])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/undo-delete-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (string) item-id
  (fn [{:keys [db]} [_ editor-id item-id]]
      (let [query        (r update.queries/get-undo-delete-item-query          db editor-id item-id)
            validator-f #(r update.validators/undo-delete-item-response-valid? db editor-id %)]
           {:db       (r ui/fake-process! db 15)
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
      ; A) Ha a "Törölt elem visszaállítása" művelet sikeres befejeződésekor a plugin rendelkezik
      ;    az útvonal elkészítéséhez szükséges tulajdonságokkal ...
      ;    ... elkészíti az elemhez tartozó útvonalat és átírányít arra.
      ;    ... az útvonal használatakor befejeződik a progress-bar elemen 15%-ig szimulált folyamat.

      ; B) Ha a "Törölt elem visszaállítása" művelet sikeres befejeződésekor a plugin NEM rendelkezik
      ;    az útvonal elkészítéséhez szükséges tulajdonságokkal ...
      ;    ... befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      {:db         (r core.events/set-recovery-mode! db editor-id)
       :dispatch-n [(if-let [item-route (r routes.subs/get-item-route db editor-id item-id)]
                            ; A)
                            [:router/go-to! item-route]
                            ; B)
                            [:ui/end-fake-process!])]}))

(a/reg-event-fx
  :item-editor/undo-delete-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ editor-id _]]
      ; A) Ha a "Törölt elem visszaállítása" művelet sikertelen befejeződésekor a body komponens
      ;    a React-fába van csatolva, ...
      ;    ... befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      ;    ... megjelenít egy értesítést.
      ;
      ; B) Ha a "Törölt elem visszaállítása" művelet sikertelen befejeződésekor a body komponens
      ;    NINCS a React-fába csatolva, ...
      ;    ... megjelenít egy értesítést.
      ;    ... feltételezi, hogy a progress-bar elemen 15%-ig szimulált folyamat befejeződött.
      (if (r mount.subs/body-did-mount? db editor-id)
          ; A)
          {:dispatch-n [[:ui/end-fake-process!]
                        [:ui/blow-bubble! {:body :failed-to-undo-delete}]]}
          ; B)
          [:ui/blow-bubble! {:body :failed-to-undo-delete}])))



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
      ; Ha az "Elem duplikálása" művelet sikeres volt, ...
      ; ... megjelenít egy értesítést.
      (let [copy-id (r update.subs/get-duplicated-item-id db editor-id server-response)]
           [:item-editor/render-item-duplicated-dialog! editor-id copy-id])))

(a/reg-event-fx
  :item-editor/duplicate-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  (fn [_ [_ editor-id _]]
      ; Ha az "Elem duplikálása" művelet sikertelen volt, ...
      ; ... megjelenít egy értesítést.
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
      {:db       (r core.events/set-recovery-mode! db editor-id)
       :dispatch [:item-editor/edit-item! editor-id item-id]}))
