
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-gestures.step-handler.effects
    (:require [x.app-core.api                         :as a :refer [r]]
              [x.app-gestures.step-handler.prototypes :as step-handler.prototypes]
              [x.app-gestures.step-handler.subs       :as step-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :gestures/step-backward!
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  [:gestures/set-backward! :my-handler]
  (fn [{:keys [db]} [_ handler-id]]
      (let [prev-dex              (r step-handler.subs/get-prev-dex          db handler-id)
            progressive-stepping? (r step-handler.subs/progressive-stepping? db handler-id)
            step-in-progress?     (r step-handler.subs/step-in-progress?     db handler-id)
            step-duration         (get-in db [:gestures :step-handler/data-items handler-id :step-duration])]
           (cond (and progressive-stepping? (not step-in-progress?))
                 {:dispatch-later
                  [{:ms 0             :dispatch [:db/set-item!   [:gestures :step-handler/data-items handler-id :step-direction] :bwd]}
                   {:ms step-duration :dispatch [:db/apply-item! [:gestures :step-handler/data-items handler-id] merge {:current-dex prev-dex :step-direction nil}]}]}
                 (and (not step-in-progress?)
                      (not progressive-stepping?))
                 [:db/set-item! [:gestures :step-handler/data-items handler-id :current-dex] prev-dex]))))

 

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :gestures/step-forward!
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  [:gestures/set-forward! :my-handler]
  (fn [{:keys [db]} [_ handler-id]]
      (let [next-dex              (r step-handler.subs/get-next-dex          db handler-id)
            progressive-stepping? (r step-handler.subs/progressive-stepping? db handler-id)
            step-in-progress?     (r step-handler.subs/step-in-progress?     db handler-id)
            step-duration         (get-in db [:gestures :step-handler/data-items handler-id :step-duration])]
           (cond (and progressive-stepping? (not step-in-progress?))
                 {:dispatch-later
                  [{:ms 0             :dispatch [:db/set-item!   [:gestures :step-handler/data-items handler-id :step-direction] :fwd]}
                   {:ms step-duration :dispatch [:db/apply-item! [:gestures :step-handler/data-items handler-id] merge {:current-dex next-dex :step-direction nil}]}]}
                 (and (not step-in-progress?)
                      (not progressive-stepping?))
                 [:db/set-item! [:gestures :step-handler/data-items handler-id :current-dex] next-dex]))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :gestures/init-step-handler!
  ; @param (keyword)(opt) handler-id
  ; @param (map) handler-props
  ;  {:autostep? (boolean)(opt)
  ;    Default: false
  ;   :infinite-stepping? (boolean)(opt)
  ;    Default: true
  ;   :paused? (boolean)(opt)
  ;    Default: false
  ;    Only w/ {:autostep? true}
  ;   :steps (* in vector)
  ;    [{:you "can"} :use #'any-kind-of types]
  ;   :step-duration (ms)(opt)
  ;    Default: DEFAULT-STEP-DURATION
  ;   :step-interval (ms)(opt)
  ;    Default: DEFAULT-STEP-INTERVAL
  ;    Only w/ {:autostep? true}}
  ;
  ; @usage
  ;  [:gestures/init-step-handler! {...}]
  ;
  ; @usage
  ;  [:gestures/init-step-handler! :my-handler {...}]
  [a/event-vector<-id]
  (fn [{:keys [db]} [_ handler-id handler-props]]
      (let [handler-props (step-handler.prototypes/handler-props-prototype handler-props)]
           (if-not (r step-handler.subs/step-handler-inited? db handler-id)
                   {:db (assoc-in db [:gestures :step-handler/data-items handler-id] handler-props)
                    :dispatch [:gestures/check-autostep! handler-id]}))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :gestures/check-autostep!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  (fn [{:keys [db]} [_ handler-id]]
      (if (r step-handler.subs/autostep? db handler-id)
          (let [step-timeout (r step-handler.subs/get-step-timeout db handler-id)]
               {:dispatch-later [{:ms step-timeout :dispatch [:gestures/step-forward!   handler-id]}
                                 {:ms step-timeout :dispatch [:gestures/check-autostep! handler-id]}]}))))
