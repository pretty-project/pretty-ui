
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.keypress-handler.effects
    (:require [mid-fruits.map                            :refer [dissoc-in]]
              [x.app-core.api                            :as a :refer [r]]
              [x.app-environment.keypress-handler.events :as keypress-handler.events]
              [x.app-environment.keypress-handler.subs   :as keypress-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :environment/reg-keypress-event!
  ; @param (keyword)(opt) event-id
  ; @param (map) event-props
  ;  {:key-code (integer)
  ;   :on-keydown (metamorphic-event)(opt)
  ;   :on-keyup (metamorphic-event)(opt)
  ;   :prevent-default? (boolean)(opt)
  ;    Default: false
  ;   :required? (boolean)(opt)
  ;    A {:required? true} beállítással regisztrált keypress események a kezelő {:type-mode? true}
  ;    állapotba lépése után is megtörténnek!
  ;    Default: false}
  ;
  ; @usage
  ;  [:environment/reg-keypress-event {...}]
  ;
  ; @usage
  ;  [:environment/reg-keypress-event :my-event {...}]
  ;
  ; @usage
  ;  [:environment/reg-keypress-event {:key-code 65 :on-keydown [:do-something!]}
  [a/event-vector<-id]
  (fn [{:keys [db]} [_ event-id {:keys [key-code prevent-default?] :as event-props}]]
      (if prevent-default? {:fx [:environment/prevent-keypress-default! key-code]
                            :db (r keypress-handler.events/reg-keypress-event! db event-id event-props)}
                           {:db (r keypress-handler.events/reg-keypress-event! db event-id event-props)})))

(a/reg-event-fx
  :environment/remove-keypress-event!
  ; @param (keyword) event-id
  ;
  ; @usage
  ;  [:environment/remove-keypress-event! :my-event]
  (fn [{:keys [db]} [_ event-id]]
      (if (r keypress-handler.subs/enable-default? db event-id)
          (let [key-code (get-in db [:environment :keypress-handler/data-items event-id :key-code])]
               {:fx [:environment/enable-keypress-default! key-code]
                :db (r keypress-handler.events/remove-keypress-event! db event-id)})
          {:db (r keypress-handler.events/remove-keypress-event! db event-id)})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :environment/key-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) key-code
  (fn [{:keys [db]} [_ key-code]]
      ; A {:type-mode? true} állapotba léptetett keypress-handler NEM tárolja el az aktuálisan
      ; leütött billentyűk kódjait, így csökkentve a keypress-handler erőforrásigényét írás közben.
      (if-not (r keypress-handler.subs/type-mode-enabled? db)
              {:db (assoc-in db [:environment :keypress-handler/meta-items :pressed-keys key-code] true)
               :dispatch-n (r keypress-handler.subs/get-on-keydown-events db key-code)}
              {:dispatch-n (r keypress-handler.subs/get-on-keydown-events db key-code)})))

(a/reg-event-fx
  :environment/key-released
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) key-code
  (fn [{:keys [db]} [_ key-code]]
      (if-not (r keypress-handler.subs/type-mode-enabled? db)
              {:db (dissoc-in db [:environment :keypress-handler/meta-items :pressed-keys key-code])
               :dispatch-n (r keypress-handler.subs/get-on-keyup-events db key-code)}
              {:dispatch-n (r keypress-handler.subs/get-on-keyup-events db key-code)})))
