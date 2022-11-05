
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.update.effects
    (:require [engines.item-handler.body.subs         :as body.subs]
              [engines.item-handler.core.events       :as core.events]
              [engines.item-handler.core.subs         :as core.subs]
              [engines.item-handler.routes.subs       :as routes.subs]
              [engines.item-handler.transfer.subs     :as transfer.subs]
              [engines.item-handler.update.queries    :as update.queries]
              [engines.item-handler.update.subs       :as update.subs]
              [engines.item-handler.update.validators :as update.validators]
              [engines.item-handler.update.views      :as update.views]
              [re-frame.api                           :as r :refer [r]]
              [x.app-ui.api                           :as x.ui]))



;; -- Save item effects -------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-handler/save-item!
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  [:item-handler/save-item! :my-handler]
  (fn [{:keys [db]} [_ handler-id]]
      (let [query        (r update.queries/get-save-item-query          db handler-id)
            validator-f #(r update.validators/save-item-response-valid? db handler-id %)]
           {:db       (r x.ui/fake-process! db 15)
            :dispatch [:pathom/send-query! (r core.subs/get-request-id db handler-id)
                                           {:on-success [:item-handler/item-saved       handler-id]
                                            :on-failure [:item-handler/save-item-failed handler-id]
                                            :query query :validator-f validator-f}]})))

(r/reg-event-fx :item-handler/item-saved
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ handler-id server-response]]
      ; Ha az "Elem mentése" művelet sikeres befejeződésekor ...
      ; ... megtörténik a body komponens számára esetlegesen átadott on-saved esemény.
      ; A) ... a body komponens a React-fába van csatolva, a mentett elem van megnyitva
      ;        szerkesztésre VAGY új elem mentése történt és az engine útvonal-vezérelt, ...
      ;        ... az [:item-handler/go-up! ...] esemény átirányít a base-route vagy item-route
      ;            útvonalra.
      ;        ... feltételezi, hogy az útvonal használatakor befejeződik a progress-bar elemen
      ;            15%-ig szimulált folyamat.
      ;
      ; B) a body komponens már nincs a React-fába csatolva és az engine útvonal-vezérelt, ...
      ;        ... megjelenít egy értesítést.
      ;
      ; C) ... az A) kimenetel feltételei nem teljesülnek ...
      ;        ... esetlegesen befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      ;        ... megjelenít egy értesítést.
      (if-let [route-handled? (r routes.subs/route-handled? db handler-id)]
              (let [item-id (r update.subs/get-saved-item-id db handler-id server-response)]
                   ; Új elem mentésekor szükséges eltárolni a szerver által visszaküldött elem-azonosítót,
                   ; az [:item-handler/go-to! ...] esemény számára!
                   (cond ; A)
                         (r core.subs/editing-item? db handler-id item-id) {:dispatch-n [(r update.subs/get-on-saved-event db handler-id server-response)
                                                                                         [:item-handler/go-up! handler-id]]}
                         (r core.subs/new-item?     db handler-id)         {:db          (r core.events/set-item-id! db handler-id item-id)
                                                                            :dispatch-n [(r update.subs/get-on-saved-event db handler-id server-response)
                                                                                         [:item-handler/go-up! handler-id]]}
                         ; B)
                         :handler-leaved                                  {:dispatch-n [(r update.subs/get-on-saved-event db handler-id server-response)
                                                                                        [:ui/render-bubble! ::item-saved-dialog {:body :saved}]]}))
              ; C)
              {:dispatch-if [(r x.ui/process-faked? db)
                             [:ui/end-fake-process!]]
               :dispatch-n [(r update.subs/get-on-saved-event db handler-id server-response)
                            [:ui/render-bubble! ::item-saved-dialog {:body :saved}]]})))

(r/reg-event-fx :item-handler/save-item-failed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ handler-id _]]
      ; Az "Elem mentése" művelet sikertelen befejeződésekor, ...
      ; ... megjelenít egy értesítést.
      ; ... esetlegesen befejezi a progress-bar elemen 15%-ig szimulált folyamatot.
      {:dispatch    [:ui/render-bubble! ::save-item-failed-dialog {:body :failed-to-save}]
       :dispatch-if [(r x.ui/process-faked? db)
                     [:ui/end-fake-process!]]}))



;; -- Restore discarded changes effects ---------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-handler/undo-discard-changes!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (string) item-id
  (fn [{:keys [db]} [_ handler-id item-id]]
      {:db         (r core.events/set-recovery-mode! db handler-id)
       :dispatch-n [[:ui/remove-bubble! ::changes-discarded-dialog]
                    [:item-handler/edit-item! handler-id item-id]]}))

(r/reg-event-fx :item-handler/render-changes-discarded-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (string) item-id
  (fn [{:keys [db]} [_ handler-id item-id]]
      ; Az [:item-handler/render-changes-discarded-dialog! ...] esemény paraméterként kapja az item-handler
      ; engine elhagyása előtt szerkesztett elem azonosítóját, mert az ... esemény megtörténésekor az azonosító
      ; már nem elérhető a Re-Frame adatbázisban.
      [:ui/render-bubble! ::changes-discarded-dialog
                          {:body             [update.views/changes-discarded-dialog-body handler-id item-id]
                           :on-bubble-closed [:item-handler/clean-recovery-data!         handler-id item-id]}]))
