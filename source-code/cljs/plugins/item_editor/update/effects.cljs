
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.update.effects
    (:require [plugins.item-editor.body.subs         :as body.subs]
              [plugins.item-editor.core.events       :as core.events]
              [plugins.item-editor.core.subs         :as core.subs]
              [plugins.item-editor.transfer.subs     :as transfer.subs]
              [plugins.item-editor.update.queries    :as update.queries]
              [plugins.item-editor.update.subs       :as update.subs]
              [plugins.item-editor.update.validators :as update.validators]
              [plugins.item-editor.update.views      :as update.views]
              [x.app-core.api                        :as a :refer [r]]
              [x.app-ui.api                          :as ui]))



;; -- Save item effects -------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/save-item!
  ; @param (keyword) editor-id
  ;
  ; @usage
  ;  [:item-editor/save-item! :my-editor]
  (fn [{:keys [db]} [_ editor-id]]
      (let [query        (r update.queries/get-save-item-query          db editor-id)
            validator-f #(r update.validators/save-item-response-valid? db editor-id %)]
           {:db       (r ui/fake-process! db 15)
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
      ;    vagy új elem mentése történt és a plugin rendelkezik a {:base-route "..."}
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
      (if (r body.subs/body-did-mount? db editor-id)
          ; A)
          {:dispatch-n [[:ui/end-fake-process!]
                        [:ui/render-bubble! {:body :failed-to-save}]]}
          ; B)
          [:ui/render-bubble! {:body :failed-to-save}])))



;; -- Restore discarded changes effects ---------------------------------------
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
      [:ui/render-bubble! :plugins.item-editor/changes-discarded-dialog
                          {:body             [update.views/changes-discarded-dialog-body editor-id item-id]
                           :on-bubble-closed [:item-editor/clean-recovery-data!          editor-id item-id]}]))
