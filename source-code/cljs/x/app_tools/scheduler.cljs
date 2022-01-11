
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.01.01
; Description:
; Version: v0.8.0
; Compatibility: x4.5.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.scheduler
    (:require [mid-fruits.candy :refer [param return]]
              [mid-fruits.map   :as map :refer [dissoc-in]]
              [mid-fruits.time  :as time]
              [x.app-core.api   :as a :refer [r]]
              [x.app-db.api     :as db]))



;; -- Usage -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  (a/dispatch [:tools/reg-schedule! {:minute 10 :event [:do-something!]}])

; @usage
;  (a/dispatch [:tools/reg-schedule! {:hour 3 :minute 10 :event [:do-something!]}])

; @usage
;  (a/dispatch [:tools/reg-schedule!    :my-schedule {...}])
;  (a/dispatch [:tools/remove-schedule! :my-schedule])



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

(defn- schedules->actual-events
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



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- scheduler-inited?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (boolean (get-in db (db/meta-item-path :tools/schedules :scheduler-inited?))))

(defn- any-schedule-registered?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (let [schedules (get-in db (db/path :tools/schedules))]
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
  (assoc-in db (db/path :tools/schedules schedule-id) schedule-props))

(defn- remove-schedule-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) schedule-id
  ;
  ; @return (map)
  [db [_ schedule-id]]
  (dissoc-in db (db/path :tools/schedules schedule-id)))

(defn- ->scheduler-inited
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (assoc-in db (db/meta-item-path :tools/schedules :scheduler-inited?) true))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :tools/reg-scheduler-interval!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ _]
      {:dispatch-later [{:ms (time/get-milliseconds-left-from-this-minute)}
                        :dispatch [:environment/set-interval! :scheduler/interval
                                   {:event [:tools/watch-scheduler-time!] :interval 60000}]]}))

(a/reg-event-fx
  :tools/init-scheduler!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (if-not (r scheduler-inited? db)
               ; A ->scheduler-inited státusz esemény az adatbázisba ír,
               ; hogy a közvetlenül egymás után megtörténő [:tools/reg-schedule!]
               ; események ne tudjanak egyszerre több scheduler-interval időzítőt regisztrálni
              {:db         (r ->scheduler-inited db)
               :dispatch-n [[:tools/watch-scheduler-time!]
                            [:tools/reg-scheduler-interval!]]})))

(a/reg-event-fx
  :tools/watch-scheduler-time!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [schedules (get-in db (db/path :tools/schedules))]
           {:dispatch-n (schedules->actual-events schedules)})))

(a/reg-event-fx
  :tools/reg-schedule!
  ; @param (keyword)(opt) schedule-id
  ; @param (map) schedule-props
  ;  {:hour (integer)(opt)
  ;   :minute (integer)(opt)
  ;   :event (metamorphic-event)}
  ;
  ; @usage
  ;  [:tools/reg-schedule! {:minute 10 :event [:do-something!]}]

  ; @usage
  ;  [:tools/reg-schedule! {:hour 3 :minute 10 :event [:do-something!]}]
  (fn [{:keys [db]} event-vector]
      (let [schedule-id    (a/event-vector->second-id   event-vector)
            schedule-props (a/event-vector->first-props event-vector)]
           {:db       (r store-schedule-props! db schedule-id schedule-props)
            :dispatch [:tools/init-scheduler!]})))

(a/reg-event-fx
  :tools/remove-schedule!
  ; @param (keyword) schedule-id
  (fn [{:keys [db]} [_ schedule-id]]
      {:db       (r remove-schedule-props! db schedule-id)
       :dispatch [:tools/->schedule-removed]}))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :tools/->schedule-removed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:dispatch-if [(not (r any-schedule-registered? db))
                     [:environment/clear-interval! :scheduler/interval]]}))
