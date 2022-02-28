
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.scheduler.effects
    (:require [mid-fruits.time :as time]
              [x.app-core.api  :as a :refer [r]]
              [x.app-tools.scheduler.engine :as scheduler.engine]
              [x.app-tools.scheduler.events :as scheduler.events]
              [x.app-tools.scheduler.subs   :as scheduler.subs]))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :tools/reg-scheduler-interval!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ _]
      {:dispatch-later [{:ms (time/get-milliseconds-left-from-this-minute)
                         :fx [:environment/set-interval! :scheduler/interval
                                                         {:event [:tools/watch-scheduler-time!] :interval 60000}]}]}))

(a/reg-event-fx
  :tools/init-scheduler!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (if-not (r scheduler.subs/scheduler-inited? db)
               ; A ->scheduler-inited státusz esemény az adatbázisba ír,
               ; hogy a közvetlenül egymás után megtörténő [:tools/reg-schedule!]
               ; események ne tudjanak egyszerre több scheduler-interval időzítőt regisztrálni
              {:db         (r scheduler.events/scheduler-inited db)
               :dispatch-n [[:tools/watch-scheduler-time!]
                            [:tools/reg-scheduler-interval!]]})))

(a/reg-event-fx
  :tools/watch-scheduler-time!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [schedules (get-in db [:tools :scheduler/data-items])]
           {:dispatch-n (scheduler.engine/schedules->actual-events schedules)})))

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
  [a/event-vector<-id]
  (fn [{:keys [db]} [_ schedule-id schedule-props]]
      {:db (r scheduler.events/store-schedule-props! db schedule-id schedule-props)
       :dispatch [:tools/init-scheduler!]}))

(a/reg-event-fx
  :tools/remove-schedule!
  ; @param (keyword) schedule-id
  (fn [{:keys [db]} [_ schedule-id]]
      {:db (r scheduler.events/remove-schedule-props! db schedule-id)
       :dispatch [:tools/schedule-removed]}))

(a/reg-event-fx
  :tools/schedule-removed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (if-not (r scheduler.subs/any-schedule-registered? db)
              {:fx [:environment/clear-interval! :scheduler/interval]})))
