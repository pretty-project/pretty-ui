
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.scheduler.sample
    (:require [tools.scheduler.api]
              [x.app-core.api :as a]))



;; -- Időzítő használata ------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :reg-my-schedules!
  {:dispatch-n [[:scheduler/reg-schedule! {        :minute 10 :event [:my-event]}]
                [:scheduler/reg-schedule! {:hour 3 :minute 10 :event [:my-event]}]]})



;; -- Időzítő eltávolítása ----------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :remove-my-schedule!
  {:dispatch-n [[:scheduler/reg-schedule!    :my-schedule {:minute 10 :event [:my-event]}]
                [:scheduler/remove-schedule! :my-schedule]]})
