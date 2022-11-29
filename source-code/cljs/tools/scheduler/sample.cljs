
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.scheduler.sample
    (:require [tools.scheduler.api]
              [re-frame.api :as r]))



;; -- Időzítő használata ------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :reg-my-schedules!
  {:dispatch-n [[:scheduler/reg-schedule! {        :minute 10 :event [:my-event]}]
                [:scheduler/reg-schedule! {:hour 3 :minute 10 :event [:my-event]}]]})



;; -- Időzítő eltávolítása ----------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :remove-my-schedule!
  {:dispatch-n [[:scheduler/reg-schedule!    :my-schedule {:minute 10 :event [:my-event]}]
                [:scheduler/remove-schedule! :my-schedule]]})
