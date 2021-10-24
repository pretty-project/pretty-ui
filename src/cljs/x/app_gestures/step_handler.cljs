
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.11
; Description:
; Version: v0.6.8
; Compatibility: x3.9.9



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
(def DEFAULT-STEP-DURATION    0)



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn extended-props->stepper-props
  ; @param (map) extended-props
  ;
  ; @return (map)
  [extended-props]
  (map/inherit (param extended-props)
               [:autostep? :infinite-stepping? :paused? :steps :step-duration :step-interval]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- stepper-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) stepper-props
  ;  {:autostep? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:autostep? (boolean)
  ;   :infinite-stepping? (boolean)
  ;   :paused? (boolean)
  ;   :step-duration (integer)
  ;   :step-interval (integer)}
  [{:keys [autostep?] :as stepper-props}]
  (merge {:autostep?          false
          :infinite-stepping? true
          :step-duration      DEFAULT-STEP-DURATION}
         (if autostep? {:paused?       false
                        :step-interval DEFAULT-STEP-INTERVAL})
         (param stepper-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn stepper-inited?
  ; @param (keyword) stepper-id
  ;
  ; @return (boolean)
  [db [_ stepper-id]]
  (map/nonempty? (get-in db (db/path ::steppers stepper-id))))

(defn- get-stepper-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) stepper-id
  ; @param (keyword) prop-id
  ;
  ; @return (*)
  [db [_ stepper-id prop-id]]
  (get-in db (db/path ::steppers stepper-id prop-id)))

(defn autostep?
  ; @param (keyword) stepper-id
  ;
  ; @return (boolean)
  [db [_ stepper-id]]
  (and (r get-stepper-prop db stepper-id :autostep?)
       (not (r get-stepper-prop db stepper-id :paused?))))

(defn progressive-stepping?
  ; @param (keyword) stepper-id
  ;
  ; @return (boolean)
  [db [_ stepper-id]]
  (let [step-duration (r get-stepper-prop db stepper-id :step-duration)]
       (> step-duration 0)))

(defn get-step-timeout
  ; @param (keyword) stepper-id
  ;
  ; @return (integer)
  [db [_ stepper-id]]
  (let [step-duration (r get-stepper-prop db stepper-id :step-duration)
        step-interval (r get-stepper-prop db stepper-id :step-interval)]
       (+ step-duration step-interval)))

(defn get-steps
  ; @param (keyword) stepper-id
  ;
  ; @return (vector)
  [db [_ stepper-id]]
  (r get-stepper-prop db stepper-id :steps))

(defn get-step
  ; @param (keyword) stepper-id
  ; @param (integer) dex
  ;
  ; @return (*)
  [db [_ stepper-id dex]]
  (nth (r get-steps db stepper-id) dex))

(defn get-max-dex
  ; @param (keyword) stepper-id
  ;
  ; @return (integer)
  [db [_ stepper-id]]
  (if-let [steps (r get-steps db stepper-id)]
          (let [step-count (count steps)]
               (dec step-count))
          (return 0)))

(defn get-current-dex
  ; @param (keyword) stepper-id
  ;
  ; @return (integer)
  [db [_ stepper-id]]
  (or (r get-stepper-prop db stepper-id :current-dex)
      (return 0)))

(defn max-dex-reached?
  ; @param (keyword) stepper-id
  ;
  ; @return (boolean)
  [db [_ stepper-id]]
  (= (r get-max-dex     db stepper-id)
     (r get-current-dex db stepper-id)))

(defn get-prev-dex
  ; @param (keyword) stepper-id
  ;
  ; @return (integer)
  [db [_ stepper-id]]
  (let [steps       (r get-steps       db stepper-id)
        current-dex (r get-current-dex db stepper-id)]
       (if (r get-stepper-prop db stepper-id :infinite-stepping?)
           (vector/prev-dex steps current-dex)
           (vector/dec-dex  steps current-dex))))

(defn get-next-dex
  ; @param (keyword) stepper-id
  ;
  ; @return (integer)
  [db [_ stepper-id]]
  (let [steps       (r get-steps       db stepper-id)
        current-dex (r get-current-dex db stepper-id)]
       (if (r get-stepper-prop db stepper-id :infinite-stepping?)
           (vector/next-dex steps current-dex)
           (if (r max-dex-reached? db stepper-id)
               (return current-dex)
               (vector/inc-dex steps current-dex)))))

(defn get-current-step
  ; @param (keyword) stepper-id
  ;
  ; @return (*)
  [db [_ stepper-id]]
  (let [current-dex (r get-current-dex db stepper-id)]
       (r get-step db stepper-id current-dex)))

(defn get-prev-step
  ; @param (keyword) stepper-id
  ;
  ; @return (*)
  [db [_ stepper-id]]
  (let [prev-dex (r get-prev-dex db stepper-id)]
       (r get-step db stepper-id prev-dex)))

(defn get-next-step
  ; @param (keyword) stepper-id
  ;
  ; @return (*)
  [db [_ stepper-id]]
  (let [next-dex (r get-next-dex db stepper-id)]
       (r get-step db stepper-id next-dex)))

(defn stepper-paused?
  ; @param (keyword) stepper-id
  ;
  ; @return (boolean)
  [db [_ stepper-id]]
  (r get-stepper-prop db stepper-id :paused?))

(defn get-step-count
  ; @param (keyword) stepper-id
  ;
  ; @return (integer)
  [db [_ stepper-id]]
  (count (r get-steps db stepper-id)))

(defn step-in-progress?
  ; @param (keyword) stepper-id
  ;
  ; @return (boolean)
  [db [_ stepper-id]]
  (some? (r get-stepper-prop db stepper-id :step-direction)))

(defn get-stepper-state
  ; @param (keyword) stepper-id
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
  [db [_ stepper-id]]
  {:current-dex    (r get-current-dex  db stepper-id)
   :current-step   (r get-current-step db stepper-id)
   :next-dex       (r get-next-dex     db stepper-id)
   :next-step      (r get-next-step    db stepper-id)
   :paused?        (r stepper-paused?  db stepper-id)
   :prev-dex       (r get-prev-dex     db stepper-id)
   :prev-step      (r get-prev-step    db stepper-id)
   :step-direction (r get-stepper-prop db stepper-id :step-direction)})



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- set-stepper-prop!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) stepper-id
  ; @param (keyword) prop-id
  ; @param (*) prop-value
  ;
  ; @return (map)
  [db [_ stepper-id prop-id prop-value]]
  (assoc-in db (db/path ::steppers stepper-id prop-id) prop-value))

(a/reg-event-db :x.app-gestures/set-stepper-prop! set-stepper-prop!)

(defn- remove-stepper-prop!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) stepper-id
  ; @param (keyword) prop-id
  ;
  ; @return (map)
  [db [_ stepper-id prop-id]]
  (dissoc-in db (db/path ::steppers stepper-id prop-id)))

(a/reg-event-db :x.app-gestures/remove-stepper-prop! remove-stepper-prop!)

(defn- store-stepper-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) stepper-id
  ; @param (map) stepper-props
  ;
  ; @return (map)
  [db [_ stepper-id stepper-props]]
  (assoc-in db (db/path ::steppers stepper-id) stepper-props))

(a/reg-event-db :x.app-gestures/store-stepper-props! store-stepper-props!)

(defn pause-stepper!
  ; @param (keyword) stepper-id
  ;
  ; @return (map)
  [db [_ stepper-id]]
  (r set-stepper-prop! db stepper-id :paused? true))

(a/reg-event-db :x.app-gestures/pause-stepper! pause-stepper!)

(defn run-stepper!
  ; @param (keyword) stepper-id
  ;
  ; @return (map)
  [db [_ stepper-id]]
  (r set-stepper-prop! db stepper-id :paused? false))

(a/reg-event-db :x.app-gestures/run-stepper! run-stepper!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-gestures/step-backward!
  (fn [{:keys [db]} [_ stepper-id]]
      (let [prev-dex              (r get-prev-dex          db stepper-id)
            progressive-stepping? (r progressive-stepping? db stepper-id)
            step-duration         (r get-stepper-prop      db stepper-id :step-duration)
            step-in-progress?     (r step-in-progress?     db stepper-id)]
           (cond (and (not   step-in-progress?)
                      (param progressive-stepping?))
                 {:dispatch-later
                  [{:ms 0             :dispatch [:x.app-gestures/set-stepper-prop!    stepper-id :step-direction :bwd]}
                   {:ms step-duration :dispatch [:x.app-gestures/set-stepper-prop!    stepper-id :current-dex prev-dex]}
                   {:ms step-duration :dispatch [:x.app-gestures/remove-stepper-prop! stepper-id :step-direction]}]}
                 (and (not step-in-progress?)
                      (not progressive-stepping?))
                 [:x.app-gestures/set-stepper-prop! stepper-id :current-dex prev-dex]))))

(a/reg-event-fx
  :x.app-gestures/step-forward!
  (fn [{:keys [db]} [_ stepper-id]]
      (let [next-dex              (r get-next-dex          db stepper-id)
            progressive-stepping? (r progressive-stepping? db stepper-id)
            step-duration         (r get-stepper-prop      db stepper-id :step-duration)
            step-in-progress?     (r step-in-progress?     db stepper-id)]
           (cond (and (not   step-in-progress?)
                      (param progressive-stepping?))
                 {:dispatch-later
                  [{:ms 0             :dispatch [:x.app-gestures/set-stepper-prop!    stepper-id :step-direction :fwd]}
                   {:ms step-duration :dispatch [:x.app-gestures/set-stepper-prop!    stepper-id :current-dex next-dex]}
                   {:ms step-duration :dispatch [:x.app-gestures/remove-stepper-prop! stepper-id :step-direction]}]}
                 (and (not step-in-progress?)
                      (not progressive-stepping?))
                 [:x.app-gestures/set-stepper-prop! stepper-id :current-dex next-dex]))))

(a/reg-event-fx
  :x.app-gestures/run-autostep?!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ stepper-id]]
      (if (r autostep? db stepper-id)
          (let [step-timeout (r get-step-timeout db stepper-id)]
               {:dispatch-later
                [{:ms step-timeout :dispatch [:x.app-gestures/step-forward!  stepper-id]}
                 {:ms step-timeout :dispatch [:x.app-gestures/run-autostep?! stepper-id]}]}))))

(a/reg-event-fx
  :x.app-gestures/init-stepper!
  ; @param (keyword)(opt) stepper-id
  ; @param (map) stepper-props
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
      (let [stepper-id    (a/event-vector->second-id   event-vector)
            stepper-props (a/event-vector->first-props event-vector)
            stepper-props (a/prot stepper-props stepper-props-prototype)]
           (if-not (r stepper-inited? db stepper-id)
                   {:dispatch-n [[:x.app-gestures/store-stepper-props! stepper-id stepper-props]
                                 [:x.app-gestures/run-autostep?!       stepper-id]]}))))
