
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.api
    (:require [x.core.connection-handler.effects]
              [x.core.debug-handler.side-effects]
              [x.core.error-handler.effects]
              [x.core.error-handler.side-effects]
              [x.core.load-handler.effects]
              [x.core.load-handler.events]
              [x.core.load-handler.lifecycles]
              [x.core.load-handler.subs]
              [x.core.login-handler.effects]
              [x.core.transfer-handler.effects]
              [x.core.transfer-handler.events]
              [x.core.build-handler.side-effects     :as build-handler.side-effects]
              [x.core.build-handler.subs             :as build-handler.subs]
              [x.core.cache-handler.helpers          :as cache-handler.helpers]
              [x.core.config-handler.subs            :as config-handler.subs]
              [x.core.debug-handler.events           :as debug-handler.events]
              [x.core.debug-handler.subs             :as debug-handler.subs]
              [x.core.lifecycle-handler.side-effects :as lifecycle-handler.side-effects]
              [x.core.lifecycle-handler.subs         :as lifecycle-handler.subs]
              [x.core.print-handler.side-effects     :as print-handler.side-effects]
              [x.core.process-handler.events         :as process-handler.events]
              [x.core.process-handler.subs           :as process-handler.subs]
              [x.core.time-handler.subs              :as time-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.core.build-handler.side-effects
(def app-build build-handler.side-effects/app-build)

; x.core.build-handler.subs
(def get-app-build build-handler.subs/get-app-build)

; x.core.cache-handler.helpers
(def cache-control-uri cache-handler.helpers/cache-control-uri)

; x.core.config-handler.subs
(def get-app-config      config-handler.subs/get-app-config)
(def get-app-config-item config-handler.subs/get-app-config-item)

; x.core.debug-handler.events
(def set-debug-mode! debug-handler.events/set-debug-mode!)

; x.core.debug-handler.subs
(def get-debug-mode       debug-handler.subs/get-debug-mode)
(def debug-mode-detected? debug-handler.subs/debug-mode-detected?)

; x.core.lifecycle-handler.side-effects
(def reg-lifecycles! lifecycle-handler.side-effects/reg-lifecycles!)

; x.core.lifecycle-handler.subs
(def get-period-events lifecycle-handler.subs/get-period-events)

; x.core.print-handler.side-effects
(def console print-handler.side-effects/console)

; x.core.process-handler.events
(def set-process-progress! process-handler.events/set-process-progress!)
(def set-process-status!   process-handler.events/set-process-status!)
(def set-process-activity! process-handler.events/set-process-activity!)

; x.core.process-handler.subs
(def get-process-status    process-handler.subs/get-process-status)
(def process-ready?        process-handler.subs/process-ready?)
(def process-preparing?    process-handler.subs/process-preparing?)
(def process-in-progress?  process-handler.subs/process-in-progress?)
(def process-success?      process-handler.subs/process-success?)
(def process-failured?     process-handler.subs/process-failured?)
(def process-blocked?      process-handler.subs/process-blocked?)
(def get-process-activity  process-handler.subs/get-process-activity)
(def process-inactive?     process-handler.subs/process-inactive?)
(def process-active?       process-handler.subs/process-active?)
(def process-idle?         process-handler.subs/process-idle?)
(def process-stalled?      process-handler.subs/process-stalled?)
(def start-process?        process-handler.subs/start-process?)
(def get-process-progress  process-handler.subs/get-process-progress)
(def process-done?         process-handler.subs/process-done?)

; x.core.time-handler.subs
(def get-server-time time-handler.subs/get-server-time)
