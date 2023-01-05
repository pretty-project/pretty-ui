
(ns tools.scheduler.effects
    (:require [re-frame.api            :as r :refer [r]]
              [time.api                :as time]
              [tools.scheduler.events  :as events]
              [tools.scheduler.helpers :as helpers]
              [tools.scheduler.subs    :as subs]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :scheduler/reg-interval!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ _]
      {:dispatch-later [{:ms (time/get-milliseconds-left-from-this-minute)
                         :fx [:x.environment/set-interval! :scheduler/interval
                                                           {:event [:scheduler/watch-time!] :interval 60000}]}]}))

(r/reg-event-fx :scheduler/init-scheduler!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (if-not (r subs/inited? db)
               ; A scheduler-inited státusz esemény az adatbázisba ír,
               ; hogy a közvetlenül egymás után megtörténő [:scheduler/reg-schedule!]
               ; események ne tudjanak egyszerre több scheduler-interval időzítőt regisztrálni
              {:db         (r events/scheduler-inited db)
               :dispatch-n [[:scheduler/watch-time!]
                            [:scheduler/reg-interval!]]})))

(r/reg-event-fx :scheduler/watch-time!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [schedules (get-in db [:tools :scheduler/data-items])]
           {:dispatch-n (helpers/schedules->actual-events schedules)})))

(r/reg-event-fx :scheduler/reg-schedule!
  ; @param (keyword)(opt) schedule-id
  ; @param (map) schedule-props
  ; {:hour (integer)(opt)
  ;  :minute (integer)(opt)
  ;  :event (metamorphic-event)}
  ;
  ; @usage
  ; [:scheduler/reg-schedule! {:minute 10 :event [:my-event]}]

  ; @usage
  ; [:scheduler/reg-schedule! {:hour 3 :minute 10 :event [:my-event]}]
  [a/event-vector<-id]
  (fn [{:keys [db]} [_ schedule-id schedule-props]]
      {:db       (r events/store-schedule-props! db schedule-id schedule-props)
       :dispatch [:scheduler/init-scheduler!]}))

(r/reg-event-fx :scheduler/remove-schedule!
  ; @param (keyword) schedule-id
  (fn [{:keys [db]} [_ schedule-id]]
      {:db       (r events/remove-schedule-props! db schedule-id)
       :dispatch [:scheduler/schedule-removed]}))

(r/reg-event-fx :scheduler/schedule-removed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (if-not (r subs/any-schedule-registered? db)
              {:fx [:x.environment/clear-interval! :scheduler/interval]})))
