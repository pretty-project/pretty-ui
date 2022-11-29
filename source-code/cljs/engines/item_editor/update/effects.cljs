
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-editor.update.effects
    (:require [engines.item-editor.body.subs         :as body.subs]
              [engines.item-editor.core.events       :as core.events]
              [engines.item-editor.core.subs         :as core.subs]
              [engines.item-editor.routes.subs       :as routes.subs]
              [engines.item-editor.transfer.subs     :as transfer.subs]
              [engines.item-editor.update.queries    :as update.queries]
              [engines.item-editor.update.subs       :as update.subs]
              [engines.item-editor.update.validators :as update.validators]
              [engines.item-editor.update.views      :as update.views]
              [re-frame.api                          :as r :refer [r]]
              [x.ui.api                              :as x.ui]))



;; -- Save item effects -------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-editor/save-item!
  ; @param (keyword) editor-id
  ;
  ; @usage
  ;  [:item-editor/save-item! :my-editor]
  (fn [{:keys [db]} [_ editor-id]]
      (let [query        (r update.queries/get-save-item-query          db editor-id)
            validator-f #(r update.validators/save-item-response-valid? db editor-id %)]
           {:db       (r x.ui/fake-process! db 15)
            :dispatch [:pathom/send-query! (r core.subs/get-request-id db editor-id)
                                           {:display-progress? false
                                            :on-success [:item-editor/item-saved       editor-id]
                                            :on-failure [:item-editor/save-item-failed editor-id]
                                            :query query :validator-f validator-f}]})))

(r/reg-event-fx :item-editor/item-saved
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ editor-id server-response]]
      ; Ha az "Elem mentése" művelet sikeres befejeződésekor ...
      ; ... megtörténik a body komponens számára esetlegesen átadott on-saved esemény.
      ; (A) ... a body komponens a React-fába van csatolva, a mentett elem van megnyitva
      ;         szerkesztésre VAGY új elem mentése történt és az engine útvonal-vezérelt, ...
      ;         ... az [:item-editor/go-up! ...] esemény átirányít a base-route vagy item-route
      ;             útvonalra.
      ;         ... feltételezi, hogy az útvonal használatakor befejeződik a progress-bar elemen
      ;             15%-ig szimulált folyamat.
      ;
      ; (B) a body komponens már nincs a React-fába csatolva és az engine útvonal-vezérelt, ...
      ;         ... megjelenít egy értesítést.
      ;
      ; (C) ... az (A) kimenetel feltételei nem teljesülnek ...
      ;         ... esetlegesen befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      ;         ... megjelenít egy értesítést.
      (if-let [route-handled? (r routes.subs/route-handled? db editor-id)]
              (let [item-id (r update.subs/get-saved-item-id db editor-id server-response)]
                   ; Új elem mentésekor szükséges eltárolni a szerver által visszaküldött elem-azonosítót,
                   ; az [:item-editor/go-to! ...] esemény számára!
                   (cond ; (A)
                         (r core.subs/editing-item? db editor-id item-id) {:dispatch-n [(r update.subs/get-on-saved-event db editor-id server-response)
                                                                                        [:item-editor/go-up! editor-id]]}
                         (r core.subs/new-item?     db editor-id)         {:db          (r core.events/set-item-id! db editor-id item-id)
                                                                           :dispatch-n [(r update.subs/get-on-saved-event db editor-id server-response)
                                                                                        [:item-editor/go-up! editor-id]]}
                         ; (B)
                         :editor-leaved                                   {:dispatch-n [(r update.subs/get-on-saved-event db editor-id server-response)
                                                                                        [:x.ui/render-bubble! ::item-saved-dialog {:body :saved}]]}))
              ; (C)
              {:dispatch-if [(r x.ui/process-faked? db)
                             [:x.ui/end-fake-process!]]
               :dispatch-n [(r update.subs/get-on-saved-event db editor-id server-response)
                            [:x.ui/render-bubble! ::item-saved-dialog {:body :saved}]]})))

(r/reg-event-fx :item-editor/save-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ editor-id _]]
      ; Az "Elem mentése" művelet sikertelen befejeződésekor, ...
      ; ... megjelenít egy értesítést.
      ; ... esetlegesen befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      {:dispatch    [:x.ui/render-bubble! ::save-item-failed-dialog {:body :failed-to-save}]
       :dispatch-if [(r x.ui/process-faked? db)
                     [:x.ui/end-fake-process!]]}))



;; -- Restore discarded changes effects ---------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-editor/undo-discard-changes!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (string) item-id
  (fn [{:keys [db]} [_ editor-id item-id]]
      {:db         (r core.events/set-recovery-mode! db editor-id)
       :dispatch-n [[:x.ui/remove-bubble! ::changes-discarded-dialog]
                    [:item-editor/edit-item! editor-id item-id]]}))

(r/reg-event-fx :item-editor/render-changes-discarded-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (string) item-id
  (fn [{:keys [db]} [_ editor-id item-id]]
      ; Az [:item-editor/render-changes-discarded-dialog! ...] esemény paraméterként kapja az item-editor
      ; engine elhagyása előtt szerkesztett elem azonosítóját, mert az ... esemény megtörténésekor az azonosító
      ; már nem elérhető a Re-Frame adatbázisban.
      [:x.ui/render-bubble! ::changes-discarded-dialog
                            {:body             [update.views/changes-discarded-dialog-body editor-id item-id]
                             :on-bubble-closed [:item-editor/clean-recovery-data!          editor-id item-id]}]))
