
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.scheduler.events
    (:require [mid-fruits.map :refer [dissoc-in]]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-schedule-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) schedule-id
  ; @param (map) schedule-props
  ;
  ; @return (map)
  [db [_ schedule-id schedule-props]]
  (assoc-in db [:tools :schedules/data-items schedule-id] schedule-props))

(defn remove-schedule-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) schedule-id
  ;
  ; @return (map)
  [db [_ schedule-id]]
  (dissoc-in db [:tools :schedules/data-items schedule-id]))

(defn scheduler-inited
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (assoc-in db [:tools :schedules/meta-items :scheduler-inited?] true))
