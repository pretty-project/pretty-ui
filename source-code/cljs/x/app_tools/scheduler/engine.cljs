
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.scheduler.engine
    (:require [mid-fruits.candy :refer [param return]]
              [mid-fruits.time  :as time]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn schedule-actual?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) schedule-props
  ;  {:hour (integer)(opt)
  ;   :minute (integer)(opt)}
  ;
  ; @return (boolean)
  [{:keys [hour minute]}]
  (let [hours   (time/get-hours)
        minutes (time/get-minutes)]
       (and (or (nil? hour)   (= hour hours))
            (or (nil? minute) (= minute minutes)))))

(defn schedules->actual-events
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) schedules
  ;
  ; @return (vector)
  [schedules]
  (letfn [(f [events schedule-id {:keys [event] :as schedule-props}]
             (if (schedule-actual? schedule-props)
                 (conj   events event)
                 (return events)))]
         (reduce-kv f [] schedules)))
