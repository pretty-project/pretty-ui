
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.api
    (:require [x.app-core.connection-handler.effects]
              [x.app-core.debug-handler.side-effects]
              [x.app-core.error-handler.effects]
              [x.app-core.error-handler.side-effects]
              [x.app-core.load-handler.effects]
              [x.app-core.load-handler.events]
              [x.app-core.load-handler.lifecycles]
              [x.app-core.load-handler.subs]
              [x.app-core.login-handler.effects]
              [x.app-core.transfer-handler.effects]
              [x.app-core.transfer-handler.events]
              [x.app-core.build-handler.side-effects     :as build-handler.side-effects]
              [x.app-core.build-handler.subs             :as build-handler.subs]
              [x.app-core.cache-handler.helpers          :as cache-handler.helpers]
              [x.app-core.config-handler.subs            :as config-handler.subs]
              [x.app-core.debug-handler.events           :as debug-handler.events]
              [x.app-core.debug-handler.subs             :as debug-handler.subs]
              [x.app-core.engine                         :as engine]
              [x.app-core.event-handler                  :as event-handler]
              [x.app-core.lifecycle-handler.side-effects :as lifecycle-handler.side-effects]
              [x.app-core.lifecycle-handler.subs         :as lifecycle-handler.subs]
              [x.app-core.print-handler.side-effects     :as print-handler.side-effects]
              [x.app-core.process-handler.events         :as process-handler.events]
              [x.app-core.process-handler.subs           :as process-handler.subs]
              [x.app-core.transfer-handler.subs          :as transfer-handler.subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-core.build-handler.side-effects
(def app-build build-handler.side-effects/app-build)

; x.app-core.build-handler.subs
(def get-app-build build-handler.subs/get-app-build)

; x.app-core.cache-handler.helpers
(def cache-control-uri cache-handler.helpers/cache-control-uri)

; x.app-core.config-handler.subs
(def get-app-config      config-handler.subs/get-app-config)
(def get-app-config-item config-handler.subs/get-app-config-item)

; x.app-core.debug-handler.events
(def set-debug-mode! debug-handler.events/set-debug-mode!)

; x.app-core.debug-handler.subs
(def get-debug-mode       debug-handler.subs/get-debug-mode)
(def debug-mode-detected? debug-handler.subs/debug-mode-detected?)

; x.app-core.engine
(def dom-value     engine/dom-value)
(def id            engine/id)
(def get-namespace engine/get-namespace)

; x.app-core.event-handler
(def event-vector?              event-handler/event-vector?)
(def subscription-vector?       event-handler/subscription-vector?)
(def event-vector->event-id     event-handler/event-vector->event-id)
(def cofx->event-vector         event-handler/cofx->event-vector)
(def cofx->event-id             event-handler/cofx->event-id)
(def context->event-vector      event-handler/context->event-vector)
(def context->event-id          event-handler/context->event-id)
(def context->db-before-effect  event-handler/context->db-before-effect)
(def context->db-after-effect   event-handler/context->db-after-effect)
(def event-vector<-params       event-handler/event-vector<-params)
(def metamorphic-event<-params  event-handler/metamorphic-event<-params)
(def merge-effects-maps         event-handler/merge-effects-maps)
(def ->interceptor              event-handler/->interceptor)
(def inject-cofx                event-handler/inject-cofx)
(def reg-cofx                   event-handler/reg-cofx)
(def reg-sub                    event-handler/reg-sub)
(def event-vector<-id           event-handler/event-vector<-id)
(def debug!                     event-handler/debug!)
(def reg-event-db               event-handler/reg-event-db)
(def reg-event-fx               event-handler/reg-event-fx)
(def reg-fx                     event-handler/reg-fx)
(def dispatch                   event-handler/dispatch)
(def dispatch-sync              event-handler/dispatch-sync)
(def dispatch-last              event-handler/dispatch-last)
(def dispatch-once              event-handler/dispatch-once)
(def dispatch-n                 event-handler/dispatch-n)
(def dispatch-later             event-handler/dispatch-later)
(def dispatch-if                event-handler/dispatch-if)
(def dispatch-cond              event-handler/dispatch-cond)
(def dispatch-tick              event-handler/dispatch-tick)
(def subscribe                  event-handler/subscribe)
(def subscribed                 event-handler/subscribed)
(def fx                         event-handler/fx)
(def fx-n                       event-handler/fx-n)
(def r                          event-handler/r)

; x.app-core.lifecycle-handler.side-effects
(def reg-lifecycles! lifecycle-handler.side-effects/reg-lifecycles!)

; x.app-core.lifecycle-handler.subs
(def get-period-events lifecycle-handler.subs/get-period-events)

; x.app-core.print-handler.side-effects
(def console print-handler.side-effects/console)

; x.app-core.process-handler.events
(def set-process-progress! process-handler.events/set-process-progress!)
(def set-process-status!   process-handler.events/set-process-status!)
(def set-process-activity! process-handler.events/set-process-activity!)

; x.app-core.process-handler.subs
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

; x.app-core.transfer-handler.subs
(def get-transfer-data transfer-handler.subs/get-transfer-data)
(def get-transfer-item transfer-handler.subs/get-transfer-item)
