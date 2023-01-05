
(ns tools.scheduler.subs
    (:require [map.api :as map]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn scheduler-inited?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (boolean (get-in db [:tools :schedules/meta-items :scheduler-inited?])))

(defn any-schedule-registered?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (let [schedules (get-in db [:tools :schedules/data-items])]
       (map/nonempty? schedules)))
