
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.03.01
; Description:
; Version: v0.4.6
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.api
    (:require [x.app-core.error-handler]
              [x.app-core.build-handler      :as build-handler]
              [x.app-core.cache-handler      :as cache-handler]
              [x.app-core.config-handler     :as config-handler]
              [x.app-core.connection-handler :as connection-handler]
              [x.app-core.debug-handler      :as debug-handler]
              [x.app-core.engine             :as engine]
              [x.app-core.event-handler      :as event-handler]
              [x.app-core.lifecycle-handler  :as lifecycle-handler]
              [x.app-core.login-handler      :as login-handler]
              [x.app-core.print-handler      :as print-handler]
              [x.app-core.process-handler    :as process-handler]
              [x.app-core.transfer-handler   :as transfer-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-core.build-handler
(def app-build build-handler/app-build)

; x.app-core.cache-handler
(def cache-control-uri cache-handler/cache-control-uri)

; x.app-core.config-handler
(def get-app-config      config-handler/get-app-config)
(def get-app-config-item config-handler/get-app-config-item)

; x.app-core.debug-handler
(def get-debug-mode       debug-handler/get-debug-mode)
(def debug-mode-detected? debug-handler/debug-mode-detected?)
(def set-debug-mode!      debug-handler/set-debug-mode!)

; x.app-core.engine
(def dom-value     engine/dom-value)
(def id            engine/id)
(def get-namespace engine/get-namespace)

; x.app-core.event-handler
(def event-vector?              event-handler/event-vector?)
(def subscription-vector?       event-handler/subscription-vector?)
(def event-vector->param-vector event-handler/event-vector->param-vector)
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
(def r                          event-handler/r)

; x.app-core.lifecycle-handler
(def get-period-events lifecycle-handler/get-period-events)
(def reg-lifecycles!   lifecycle-handler/reg-lifecycles!)

; x.app-core.print-handler
(def console print-handler/console)

; x.app-core.process-handler
(def get-process-status    process-handler/get-process-status)
(def process-ready?        process-handler/process-ready?)
(def process-preparing?    process-handler/process-preparing?)
(def process-in-progress?  process-handler/process-in-progress?)
(def process-success?      process-handler/process-success?)
(def process-failured?     process-handler/process-failured?)
(def process-blocked?      process-handler/process-blocked?)
(def get-process-activity  process-handler/get-process-activity)
(def process-inactive?     process-handler/process-inactive?)
(def process-active?       process-handler/process-active?)
(def process-idle?         process-handler/process-idle?)
(def process-stalled?      process-handler/process-stalled?)
(def start-process?        process-handler/start-process?)
(def get-process-progress  process-handler/get-process-progress)
(def process-done?         process-handler/process-done?)
(def set-process-progress! process-handler/set-process-progress!)
(def set-process-status!   process-handler/set-process-status!)
(def set-process-activity! process-handler/set-process-activity!)
(def clear-process!        process-handler/clear-process!)

; x.app-core.transfer-handler
(def get-transfer-data transfer-handler/get-transfer-data)
