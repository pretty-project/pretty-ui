
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.01.01
; Description:
; Version: v0.6.2
; Compatibility: x4.2.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.scheduler
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.map    :as map :refer [dissoc-in]]
              [mid-fruits.time   :as time]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]
              [x.app-db.api      :as db]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- schedule-actual?
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



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- schedules->actual-events
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) schedules
  ;
  ; @return (vector)
  [schedules]
  (reduce-kv (fn [events schedule-id {:keys [event] :as schedule-props}]
                 (if (schedule-actual? schedule-props)
                     (vector/conj-item events event)
                     (return events)))
             (param [])
             (param schedules)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- scheduler-inited?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (boolean (get-in db (db/meta-item-path ::schedules :scheduler-inited?))))

(defn- any-schedule-registered?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (let [schedules (get-in db (db/path ::schedules))]
       (map/nonempty? schedules)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- store-schedule-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) schedule-id
  ; @param (map) schedule-props
  ;
  ; @return (map)
  [db [_ schedule-id schedule-props]]
  (assoc-in db (db/path ::schedules schedule-id)
               (param schedule-props)))

(a/reg-event-db ::store-schedule-props! store-schedule-props!)

(defn- remove-schedule-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) schedule-id
  ;
  ; @return (map)
  [db [_ schedule-id]]
  (dissoc-in db (db/path ::schedules schedule-id)))

(a/reg-event-db ::remove-schedule-props! remove-schedule-props!)

(defn- ->scheduler-inited
  [db _]
  (assoc-in db (db/meta-item-path ::schedules :scheduler-inited?)
               (param true)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::reg-scheduler-interval!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ _]
      {:dispatch-later [{:ms (time/get-milliseconds-left-from-this-minute) :dispatch
                         [:x.app-environment.window-handler/set-interval!
                          ::interval {:event [::watch-time!] :interval 60000}]}]}))

(a/reg-event-fx
  ::init-scheduler!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (if-not (r scheduler-inited? db)

               ; A ->scheduler-inited státusz esemény az adatbázisba ír, hogy
               ; a közvetlenül egymás után megtörténő [::reg-schedule!]
               ; események ne tudjanak egyszerre több scheduler-interval
               ; időzítőt regisztrálni
              {:db (r ->scheduler-inited db)

               :dispatch-n [[::watch-time!]
                            [::reg-scheduler-interval!]]})))

(a/reg-event-fx
  ::watch-time!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [schedules (get-in db (db/path ::schedules))]
           {:dispatch-n (schedules->actual-events schedules)})))

(a/reg-event-fx
  ::reg-schedule!
  ; @param (keyword)(opt) schedule-id
  ; @param (map) schedule-props
  ;  {:hour (integer)(opt)
  ;   :minute (integer)(opt)
  ;   :event (metamorphic-event)}
  ;
  ; @usage
  ;  [::reg-schedule! {:minute 10 :event [:do-something!]}]
  ;  [::reg-schedule! {:hour 3 :minute 10 :event [:do-something!]}]
  (fn [_ event-vector]
      (let [schedule-id    (a/event-vector->second-id   event-vector)
            schedule-props (a/event-vector->first-props event-vector)]
           {:dispatch-n [[::store-schedule-props! schedule-id schedule-props]
                         [::init-scheduler!]]})))

(a/reg-event-fx
  ::remove-schedule!
  ; @param (keyword) schedule-id
  (fn [_ [_ schedule-id]]
      {:dispatch-n [[::remove-schedule-props! schedule-id]
                    [::->schedule-removed]]}))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::->schedule-removed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:dispatch-if [(not (r any-schedule-registered? db))
                     [:x.app-environment.window/clear-interval! ::interval]]}))
