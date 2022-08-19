

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.scheduler.sample
    (:require [x.app-tools.api]
              [x.app-core.api :as a]))



;; -- Időzítő használata ------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :reg-my-schedules!
  {:dispatch-n [[:tools/reg-schedule! {        :minute 10 :event [:my-event]}]
                [:tools/reg-schedule! {:hour 3 :minute 10 :event [:my-event]}]]})



;; -- Időzítő eltávolítása ----------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :remove-my-schedule!
  {:dispatch-n [[:tools/reg-schedule!    :my-schedule {:minute 10 :event [:my-event]}]
                [:tools/remove-schedule! :my-schedule]]})
