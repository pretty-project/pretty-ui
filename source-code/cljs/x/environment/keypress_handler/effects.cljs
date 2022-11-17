
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.environment.keypress-handler.effects
    (:require [map.api                               :refer [dissoc-in]]
              [re-frame.api                          :as r :refer [r]]
              [x.environment.keypress-handler.events :as keypress-handler.events]
              [x.environment.keypress-handler.subs   :as keypress-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.environment/reg-keypress-event!
  ; @param (keyword)(opt) event-id
  ; @param (map) event-props
  ;  {:exclusive? (boolean)(opt)
  ;    Az {:exclusive? true} beállítással regisztrált keypress események kikapcsolt
  ;    állapotban tartják az azonos billentyű-lenyomást figyelő többi eseményt.
  ;    Default: false
  ;   :key-code (integer)
  ;   :on-keydown (metamorphic-event)(opt)
  ;   :on-keyup (metamorphic-event)(opt)
  ;   :prevent-default? (boolean)(opt)
  ;    Default: false
  ;   :required? (boolean)(opt)
  ;    A {:required? true} beállítással regisztrált keypress események a kezelő
  ;    {:type-mode? true} állapotba lépése után is megtörténnek!
  ;    Default: false}
  ;
  ; @usage
  ;  [:x.environment/reg-keypress-event {...}]
  ;
  ; @usage
  ;  [:x.environment/reg-keypress-event :my-keypress-event {...}]
  ;
  ; @usage
  ;  [:x.environment/reg-keypress-event {:key-code 65 :on-keydown [:my-event]}
  [r/event-vector<-id]
  (fn [{:keys [db]} [_ event-id {:keys [key-code prevent-default?] :as event-props}]]
      (if prevent-default? {:fx [:x.environment/prevent-keypress-default! key-code]
                            :db (r keypress-handler.events/reg-keypress-event! db event-id event-props)}
                           {:db (r keypress-handler.events/reg-keypress-event! db event-id event-props)})))

(r/reg-event-fx :x.environment/remove-keypress-event!
  ; @param (keyword) event-id
  ;
  ; @usage
  ;  [:x.environment/remove-keypress-event! :my-event]
  (fn [{:keys [db]} [_ event-id]]
      (if (r keypress-handler.subs/enable-default? db event-id)
          (let [key-code (get-in db [:x.environment :keypress-handler/data-items event-id :key-code])]
               {:fx [:x.environment/enable-keypress-default! key-code]
                :db (r keypress-handler.events/remove-keypress-event! db event-id)})
          {:db (r keypress-handler.events/remove-keypress-event! db event-id)})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.environment/listen-to-pressed-key!
  ; @param (keyword)(opt) event-id
  ; @param (map) event-props
  ;  {:key-code (integer)}
  ;
  ; @usage
  ;  [:x.environment/listen-to-pressed-key! {...}]
  ;
  ; @usage
  ;  [:x.environment/listen-to-pressed-key! :my-event {...}]
  [r/event-vector<-id]
  (fn [{:keys [db]} [_ event-id {:keys [key-code]}]]
      ; Az [:x.environment/listen-to-pressed-key! ...] esemény által regisztrált billentyűk kódjait
      ; a keypress-handler {:type-mode? true} állapotba léptetve is eltárolja!
      ;
      ; Mivel a keypress-handler {:type-mode? true} állapotba léptetve NEM tárolja el a leütött
      ; billentyűk kódjait, ezért létezik az [:x.environment/listen-to-pressed-key! ...] esemény,
      ; hogy egyes billentyűket kivételként kezeljen és {:type-mode? true} állapotban is
      ; eltárolja az állapotukat.
      (let [on-keydown [:x.db/set-item!    [:x.environment :keypress-handler/meta-items :pressed-keys key-code] true]
            on-keyup   [:x.db/remove-item! [:x.environment :keypress-handler/meta-items :pressed-keys key-code]]]
           {:db (r keypress-handler.events/reg-keypress-event! db event-id {:key-code   key-code
                                                                            :on-keydown on-keydown
                                                                            :on-keyup   on-keyup
                                                                            :required?  true})})))

(r/reg-event-fx :x.environment/stop-listening-to-pressed-key!
  ; @param (keyword) event-id
  ;
  ; @usage
  ;  [:x.environment/stop-listening-to-pressed-key! :my-event]
  (fn [{:keys [db]} [_ event-id]]
      {:db (r keypress-handler.events/remove-keypress-event! db event-id)}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.environment/key-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) key-code
  (fn [{:keys [db]} [_ key-code]]
      ; A {:type-mode? true} állapotba léptetett keypress-handler NEM tárolja el az aktuálisan
      ; leütött billentyűk kódjait, így csökkentve a Re-Frame adatbázis írásainak számát és ezáltal
      ; a keypress-handler erőforrásigényét írás közben.
      (if-not (r keypress-handler.subs/type-mode-enabled? db)
              {:db         (assoc-in db [:x.environment :keypress-handler/meta-items :pressed-keys key-code] true)
               :dispatch-n (r keypress-handler.subs/get-on-keydown-events db key-code)}
              {:dispatch-n (r keypress-handler.subs/get-on-keydown-events db key-code)})))

(r/reg-event-fx :x.environment/key-released
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) key-code
  (fn [{:keys [db]} [_ key-code]]
      ; BUG#5050
      ; https://stackoverflow.com/questions/25438608/javascript-keyup-isnt-called-when-command-and-another-is-pressed
      (if-not (r keypress-handler.subs/type-mode-enabled? db)
              {:db         (dissoc-in db [:x.environment :keypress-handler/meta-items :pressed-keys key-code])
               :dispatch-n (r keypress-handler.subs/get-on-keyup-events db key-code)}
              {:dispatch-n (r keypress-handler.subs/get-on-keyup-events db key-code)})))
