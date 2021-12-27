
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.11
; Description:
; Version: v0.8.2
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-gestures.step-handler
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.map    :as map :refer [dissoc-in]]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]
              [x.app-db.api      :as db]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (ms)
(def DEFAULT-STEP-INTERVAL 5000)

; @constant (ms)
(def DEFAULT-STEP-DURATION 0)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn extended-props->step-handler-props
  ; @param (map) extended-props
  ;
  ; @return (map)
  [extended-props]
  (select-keys extended-props [:autostep? :infinite-stepping? :paused? :steps :step-duration :step-interval]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- handler-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) handler-props
  ;  {:autostep? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:autostep? (boolean)
  ;   :infinite-stepping? (boolean)
  ;   :paused? (boolean)
  ;   :step-duration (integer)
  ;   :step-interval (integer)}
  [{:keys [autostep?] :as handler-props}]
  (merge {:autostep?          false
          :infinite-stepping? true
          :step-duration      DEFAULT-STEP-DURATION}
         (if autostep? {:paused?       false
                        :step-interval DEFAULT-STEP-INTERVAL})
         (param handler-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn step-handler-inited?
  ; @param (keyword) handler-id
  ;
  ; @return (boolean)
  [db [_ handler-id]]
  (map/nonempty? (get-in db (db/path ::step-handlers handler-id))))

(defn- get-step-handler-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (keyword) prop-id
  ;
  ; @return (*)
  [db [_ handler-id prop-id]]
  (get-in db (db/path ::step-handlers handler-id prop-id)))

(defn autostep?
  ; @param (keyword) handler-id
  ;
  ; @return (boolean)
  [db [_ handler-id]]
  (and (r get-step-handler-prop      db handler-id :autostep?)
       (not (r get-step-handler-prop db handler-id :paused?))))

(defn progressive-stepping?
  ; @param (keyword) handler-id
  ;
  ; @return (boolean)
  [db [_ handler-id]]
  (let [step-duration (r get-step-handler-prop db handler-id :step-duration)]
       (> step-duration 0)))

(defn get-step-timeout
  ; @param (keyword) handler-id
  ;
  ; @return (integer)
  [db [_ handler-id]]
  (let [step-duration (r get-step-handler-prop db handler-id :step-duration)
        step-interval (r get-step-handler-prop db handler-id :step-interval)]
       (+ step-duration step-interval)))

(defn get-steps
  ; @param (keyword) handler-id
  ;
  ; @return (vector)
  [db [_ handler-id]]
  (r get-step-handler-prop db handler-id :steps))

(defn get-step
  ; @param (keyword) handler-id
  ; @param (integer) dex
  ;
  ; @return (*)
  [db [_ handler-id dex]]
  (nth (r get-steps db handler-id) dex))

(defn get-max-dex
  ; @param (keyword) handler-id
  ;
  ; @return (integer)
  [db [_ handler-id]]
  (if-let [steps (r get-steps db handler-id)]
          (let [step-count (count steps)]
               (dec step-count))
          (return 0)))

(defn get-current-dex
  ; @param (keyword) handler-id
  ;
  ; @return (integer)
  [db [_ handler-id]]
  (or (r get-step-handler-prop db handler-id :current-dex)
      (return 0)))

(defn max-dex-reached?
  ; @param (keyword) handler-id
  ;
  ; @return (boolean)
  [db [_ handler-id]]
  (= (r get-max-dex     db handler-id)
     (r get-current-dex db handler-id)))

(defn get-prev-dex
  ; @param (keyword) handler-id
  ;
  ; @return (integer)
  [db [_ handler-id]]
  (let [steps       (r get-steps       db handler-id)
        current-dex (r get-current-dex db handler-id)]
       (if (r get-step-handler-prop db handler-id :infinite-stepping?)
           (vector/prev-dex steps current-dex)
           (vector/dec-dex  steps current-dex))))

(defn get-next-dex
  ; @param (keyword) handler-id
  ;
  ; @return (integer)
  [db [_ handler-id]]
  (let [steps       (r get-steps       db handler-id)
        current-dex (r get-current-dex db handler-id)]
       (if (r get-step-handler-prop db handler-id :infinite-stepping?)
           (vector/next-dex steps current-dex)
           (if (r max-dex-reached? db handler-id)
               (return current-dex)
               (vector/inc-dex steps current-dex)))))

(defn get-current-step
  ; @param (keyword) handler-id
  ;
  ; @return (*)
  [db [_ handler-id]]
  (let [current-dex (r get-current-dex db handler-id)]
       (r get-step db handler-id current-dex)))

(defn get-prev-step
  ; @param (keyword) handler-id
  ;
  ; @return (*)
  [db [_ handler-id]]
  (let [prev-dex (r get-prev-dex db handler-id)]
       (r get-step db handler-id prev-dex)))

(defn get-next-step
  ; @param (keyword) handler-id
  ;
  ; @return (*)
  [db [_ handler-id]]
  (let [next-dex (r get-next-dex db handler-id)]
       (r get-step db handler-id next-dex)))

(defn stepping-paused?
  ; @param (keyword) handler-id
  ;
  ; @return (boolean)
  [db [_ handler-id]]
  (r get-step-handler-prop db handler-id :paused?))

(defn get-step-count
  ; @param (keyword) handler-id
  ;
  ; @return (integer)
  [db [_ handler-id]]
  (count (r get-steps db handler-id)))

(defn step-in-progress?
  ; @param (keyword) handler-id
  ;
  ; @return (boolean)
  [db [_ handler-id]]
  (some? (r get-step-handler-prop db handler-id :step-direction)))

(defn get-step-handler-state
  ; @param (keyword) handler-id
  ;
  ; @return (map)
  ;  {:current-dex (integer)
  ;   :current-step (*)
  ;   :next-dex (integer)
  ;   :next-step (*)
  ;   :paused? (boolean)
  ;   :prev-dex (integer)
  ;   :prev-step (*)
  ;   :step-direction (keyword)}
  [db [_ handler-id]]
  {:current-dex    (r get-current-dex  db handler-id)
   :current-step   (r get-current-step db handler-id)
   :next-dex       (r get-next-dex     db handler-id)
   :next-step      (r get-next-step    db handler-id)
   :paused?        (r stepping-paused? db handler-id)
   :prev-dex       (r get-prev-dex     db handler-id)
   :prev-step      (r get-prev-step    db handler-id)
   :step-direction (r get-step-handler-prop db handler-id :step-direction)})



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- set-step-handler-prop!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (keyword) prop-id
  ; @param (*) prop-value
  ;
  ; @return (map)
  [db [_ handler-id prop-id prop-value]]
  (assoc-in db (db/path ::step-handlers handler-id prop-id) prop-value))

(a/reg-event-db :gestures/set-step-handler-prop! set-step-handler-prop!)

(defn- remove-step-handler-prop!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (keyword) prop-id
  ;
  ; @return (map)
  [db [_ handler-id prop-id]]
  (dissoc-in db (db/path ::step-handlers handler-id prop-id)))

(a/reg-event-db :gestures/remove-step-handler-prop! remove-step-handler-prop!)

(defn- store-step-handler-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) handler-props
  ;
  ; @return (map)
  [db [_ handler-id handler-props]]
  (assoc-in db (db/path ::step-handlers handler-id) handler-props))

(a/reg-event-db :gestures/store-step-handler-props! store-step-handler-props!)

(defn pause-stepping!
  ; @param (keyword) handler-id
  ;
  ; @return (map)
  [db [_ handler-id]]
  (r set-step-handler-prop! db handler-id :paused? true))

(a/reg-event-db :gestures/pause-stepping! pause-stepping!)

(defn run-stepping!
  ; @param (keyword) handler-id
  ;
  ; @return (map)
  [db [_ handler-id]]
  (r set-step-handler-prop! db handler-id :paused? false))

(a/reg-event-db :gestures/run-stepping! run-stepping!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :gestures/step-backward!
  (fn [{:keys [db]} [_ handler-id]]
      (let [prev-dex              (r get-prev-dex          db handler-id)
            progressive-stepping? (r progressive-stepping? db handler-id)
            step-duration         (r get-step-handler-prop db handler-id :step-duration)
            step-in-progress?     (r step-in-progress?     db handler-id)]
           (cond (and (not   step-in-progress?)
                      (param progressive-stepping?))
                 {:dispatch-later
                  [{:ms 0             :dispatch [:gestures/set-step-handler-prop!    handler-id :step-direction :bwd]}
                   {:ms step-duration :dispatch [:gestures/set-step-handler-prop!    handler-id :current-dex prev-dex]}
                   {:ms step-duration :dispatch [:gestures/remove-step-handler-prop! handler-id :step-direction]}]}
                 (and (not step-in-progress?)
                      (not progressive-stepping?))
                 [:gestures/set-step-handler-prop! handler-id :current-dex prev-dex]))))

(a/reg-event-fx
  :gestures/step-forward!
  (fn [{:keys [db]} [_ handler-id]]
      (let [next-dex              (r get-next-dex          db handler-id)
            progressive-stepping? (r progressive-stepping? db handler-id)
            step-duration         (r get-step-handler-prop db handler-id :step-duration)
            step-in-progress?     (r step-in-progress?     db handler-id)]
           (cond (and (not   step-in-progress?)
                      (param progressive-stepping?))
                 {:dispatch-later
                  [{:ms 0             :dispatch [:gestures/set-ste-handler-prop!     handler-id :step-direction :fwd]}
                   {:ms step-duration :dispatch [:gestures/set-step-handler-prop!    handler-id :current-dex next-dex]}
                   {:ms step-duration :dispatch [:gestures/remove-step-handler-prop! handler-id :step-direction]}]}
                 (and (not step-in-progress?)
                      (not progressive-stepping?))
                 [:gestures/set-step-handler-prop! handler-id :current-dex next-dex]))))

(a/reg-event-fx
  :gestures/run-autostep?!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ handler-id]]
      (if (r autostep? db handler-id)
          (let [step-timeout (r get-step-timeout db handler-id)]
               {:dispatch-later
                [{:ms step-timeout :dispatch [:gestures/step-forward!  handler-id]}
                 {:ms step-timeout :dispatch [:gestures/run-autostep?! handler-id]}]}))))

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
  (fn [{:keys [db]} event-vector]
      (let [handler-id    (a/event-vector->second-id   event-vector)
            handler-props (a/event-vector->first-props event-vector)
            handler-props (handler-props-prototype handler-props)]
           (if-not (r step-handler-inited? db handler-id)
                   {:dispatch-n [[:gestures/store-step-handler-props! handler-id handler-props]
                                 [:gestures/run-autostep?!            handler-id]]}))))
