
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.03.01
; Description:
; Version: v0.3.0
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.api
    (:require [x.app-core.error-handler]
              [x.app-core.cache-handler      :as cache-handler]
              [x.app-core.config-handler     :as config-handler]
              [x.app-core.connection-handler :as connection-handler]
              [x.app-core.debug-handler      :as debug-handler]
              [x.app-core.engine             :as engine]
              [x.app-core.event-handler      :as event-handler]
              [x.app-core.lifecycle-handler  :as lifecycle-handler]
              [x.app-core.login-handler      :as login-handler]
              [x.app-core.process-handler    :as process-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-core.cache-handler
(def cache-control-uri cache-handler/cache-control-uri)

; x.app-core.config-handler
(def app-detail-path     config-handler/app-detail-path)
(def storage-detail-path config-handler/storage-detail-path)
(def get-app-details     config-handler/get-app-details)
(def get-app-detail      config-handler/get-app-detail)
(def get-storage-details config-handler/get-storage-details)
(def get-storage-detail  config-handler/get-storage-detail)

; x.app-core.debug-handler
(def console                   debug-handler/console)
(def query-string->debug-mode? debug-handler/query-string->debug-mode?)
(def query-string->debug-mode  debug-handler/query-string->debug-mode)
(def debug-mode?               debug-handler/debug-mode?)
(def debug-mode                debug-handler/debug-mode)
(def get-debug-mode            debug-handler/get-debug-mode)
(def debug-mode-detected?      debug-handler/debug-mode-detected?)
(def store-debug-mode!         debug-handler/store-debug-mode!)

; x.app-core.engine
(def dom-value     engine/dom-value)
(def id            engine/id)
(def prot          engine/prot)
(def sub-prot      engine/sub-prot)
(def get-namespace engine/get-namespace)

; x.app-core.event-handler
(def event-vector?                    event-handler/event-vector?)
(def subscription-vector?             event-handler/subscription-vector?)
(def event-group-vector?              event-handler/event-group-vector?)
(def event-vector->param-vector       event-handler/event-vector->param-vector)
(def event-vector->event-id           event-handler/event-vector->event-id)
(def context->event-vector            event-handler/context->event-vector)
(def context->empty-event-vector      event-handler/context->empty-event-vector)
(def context->event-props             event-handler/context->event-props)
(def context->event-id                event-handler/context->event-id)
(def context->db-before-effect        event-handler/context->db-before-effect)
(def context->db-after-effect         event-handler/context->db-after-effect)
(def context->db-inconsistent?        event-handler/context->db-inconsistent?)
(def context->error-props             event-handler/context->error-props)
(def context->error-catched?          event-handler/context->error-catched?)
(def param-vector->first-id           event-handler/param-vector->first-id)
(def param-vector->second-id          event-handler/param-vector->second-id)
(def param-vector->first-props        event-handler/param-vector->first-props)
(def param-vector->second-props       event-handler/param-vector->second-props)
(def event-vector->first-id           event-handler/event-vector->first-id)
(def event-vector->second-id          event-handler/event-vector->second-id)
(def event-vector->third-id           event-handler/event-vector->third-id)
(def event-vector->first-props        event-handler/event-vector->first-props)
(def event-vector->second-props       event-handler/event-vector->second-props)
(def event-vector<-params             event-handler/event-vector<-params)
(def metamorphic-event<-params        event-handler/metamorphic-event<-params)
(def metamorphic-effects->effects-map event-handler/metamorphic-effects->effects-map)
(def merge-effects-maps               event-handler/merge-effects-maps)
(def db                               event-handler/db)
(def ->interceptor                    event-handler/->interceptor)
(def inject-cofx                      event-handler/inject-cofx)
(def reg-cofx                         event-handler/reg-cofx)
(def reg-fx                           event-handler/reg-fx)
(def reg-sub                          event-handler/reg-sub)
(def get-event-handlers               event-handler/get-event-handlers)
(def get-event-handler                event-handler/get-event-handler)
(def event-handler-registrated?       event-handler/event-handler-registrated?)
(def reg-event-db                     event-handler/reg-event-db)
(def reg-event-fx                     event-handler/reg-event-fx)
(def reg-handled-fx                   event-handler/reg-handled-fx)
(def self-destruct!                   event-handler/self-destruct!)
(def dispatch                         event-handler/dispatch)
(def dispatch-sync                    event-handler/dispatch-sync)
(def dispatch-last                    event-handler/dispatch-last)
(def dispatch-once                    event-handler/dispatch-once)
(def dispatch-n                       event-handler/dispatch-n)
(def dispatch-some                    event-handler/dispatch-some)
(def dispatch-if                      event-handler/dispatch-if)
(def dispatch-cond                    event-handler/dispatch-cond)
(def dispatch-tick                    event-handler/dispatch-tick)
(def dispatch-later                   event-handler/dispatch-later)
(def subscribe                        event-handler/subscribe)
(def subscribed                       event-handler/subscribed)
(def r                                event-handler/r)

; x.app-core.lifecycle-handler
(def get-period-events lifecycle-handler/get-period-events)
(def reg-lifecycles    lifecycle-handler/reg-lifecycles)

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
; WARNING#4067
(def set-process-progress! process-handler/set-process-progress!)
; WARNING#4067
(def set-process-status!   process-handler/set-process-status!)
; WARNING#4067
(def set-process-activity! process-handler/set-process-activity!)
(def clear-process!        process-handler/clear-process!)
