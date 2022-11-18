
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.gestures.step-handler.effects
    (:require [re-frame.api                       :as r :refer [r]]
              [x.gestures.step-handler.prototypes :as step-handler.prototypes]
              [x.gestures.step-handler.subs       :as step-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.gestures/step-backward!
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  [:x.gestures/set-backward! :my-handler]
  (fn [{:keys [db]} [_ handler-id]]
      (let [prev-dex              (r step-handler.subs/get-prev-dex          db handler-id)
            progressive-stepping? (r step-handler.subs/progressive-stepping? db handler-id)
            step-in-progress?     (r step-handler.subs/step-in-progress?     db handler-id)
            step-duration         (get-in db [:x.gestures :step-handler/data-items handler-id :step-duration])]
           (cond (and progressive-stepping? (not step-in-progress?))
                 {:dispatch-later
                  [{:ms 0             :dispatch [:x.db/set-item!   [:x.gestures :step-handler/data-items handler-id :step-direction] :bwd]}
                   {:ms step-duration :dispatch [:x.db/apply-item! [:x.gestures :step-handler/data-items handler-id] merge {:current-dex prev-dex :step-direction nil}]}]}
                 (and (not step-in-progress?)
                      (not progressive-stepping?))
                 [:x.db/set-item! [:x.gestures :step-handler/data-items handler-id :current-dex] prev-dex]))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.gestures/step-forward!
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  [:x.gestures/set-forward! :my-handler]
  (fn [{:keys [db]} [_ handler-id]]
      (let [next-dex              (r step-handler.subs/get-next-dex          db handler-id)
            progressive-stepping? (r step-handler.subs/progressive-stepping? db handler-id)
            step-in-progress?     (r step-handler.subs/step-in-progress?     db handler-id)
            step-duration         (get-in db [:x.gestures :step-handler/data-items handler-id :step-duration])]
           (cond (and progressive-stepping? (not step-in-progress?))
                 {:dispatch-later
                  [{:ms 0             :dispatch [:x.db/set-item!   [:x.gestures :step-handler/data-items handler-id :step-direction] :fwd]}
                   {:ms step-duration :dispatch [:x.db/apply-item! [:x.gestures :step-handler/data-items handler-id] merge {:current-dex next-dex :step-direction nil}]}]}
                 (and (not step-in-progress?)
                      (not progressive-stepping?))
                 [:x.db/set-item! [:x.gestures :step-handler/data-items handler-id :current-dex] next-dex]))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.gestures/init-step-handler!
  ; @param (keyword)(opt) handler-id
  ; @param (map) handler-props
  ;  {:autostep? (boolean)(opt)
  ;    Default: false
  ;   :infinite-stepping? (boolean)(opt)
  ;    Default: true
  ;   :paused? (boolean)(opt)
  ;    Default: false
  ;    W/ {:autostep? true}
  ;   :steps (* in vector)
  ;    [{:you "can"} :use #'any-kind-of types]
  ;   :step-duration (ms)(opt)
  ;    Default: DEFAULT-STEP-DURATION
  ;   :step-interval (ms)(opt)
  ;    Default: DEFAULT-STEP-INTERVAL
  ;    W/ {:autostep? true}}
  ;
  ; @usage
  ;  [:x.gestures/init-step-handler! {...}]
  ;
  ; @usage
  ;  [:x.gestures/init-step-handler! :my-handler {...}]
  [r/event-vector<-id]
  (fn [{:keys [db]} [_ handler-id handler-props]]
      (let [handler-props (step-handler.prototypes/handler-props-prototype handler-props)]
           (if-not (r step-handler.subs/step-handler-inited? db handler-id)
                   {:db       (assoc-in db [:x.gestures :step-handler/data-items handler-id] handler-props)
                    :dispatch [:x.gestures/check-autostep! handler-id]}))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.gestures/check-autostep!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  (fn [{:keys [db]} [_ handler-id]]
      (if (r step-handler.subs/autostep? db handler-id)
          (let [step-timeout (r step-handler.subs/get-step-timeout db handler-id)]
               {:dispatch-later [{:ms step-timeout :dispatch [:x.gestures/step-forward!   handler-id]}
                                 {:ms step-timeout :dispatch [:x.gestures/check-autostep! handler-id]}]}))))