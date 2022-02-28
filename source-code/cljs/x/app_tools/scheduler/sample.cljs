
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.scheduler.sample
    (:require [x.app-tools.api]
              [x.app-core.api :as a]))



;; -- Időzítő használata ------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :reg-my-schedules!
  {:dispatch-n [[:tools/reg-schedule! {        :minute 10 :event [:do-something!]}]
                [:tools/reg-schedule! {:hour 3 :minute 10 :event [:do-something!]}]]})



;; -- Időzítő eltávolítása ----------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :remove-my-schedule!
  {:dispatch-n [[:tools/reg-schedule!    :my-schedule {:minute 10 :event [:do-something!]}]
                [:tools/remove-schedule! :my-schedule]]})
