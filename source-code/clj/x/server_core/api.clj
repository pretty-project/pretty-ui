
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.01.01
; Description:
; Version: v1.2.6
; Compatibility: x4.5.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.api
    (:require [x.server-core.build-handler]
              [x.server-core.database-handler]
              [x.server-core.error-handler]
              [x.server-core.middleware-handler]
              [x.server-core.resource-handler]
              [x.server-core.server-handler]
              [x.server-core.build-handler     :as build-handler]
              [x.server-core.cache-handler     :as cache-handler]
              [x.server-core.config-handler    :as config-handler]
              [x.server-core.debug-handler     :as debug-handler]
              [x.server-core.engine            :as engine]
              [x.server-core.event-handler     :as event-handler]
              [x.server-core.lifecycle-handler :as lifecycle-handler]
              [x.server-core.router-handler    :as router-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.server-core.build-handler
(def ->app-built build-handler/->app-built)

; x.server-core.cache-handler
(def request->app-cached? cache-handler/request->app-cached?)
(def cache-control-uri    cache-handler/cache-control-uri)

; x.server-core.config-handler
(def APP-CONFIG-FILEPATH    config-handler/APP-CONFIG-FILEPATH)
(def SITE-CONFIG-FILEPATH   config-handler/SITE-CONFIG-FILEPATH)
(def SERVER-CONFIG-FILEPATH config-handler/SERVER-CONFIG-FILEPATH)
(def get-app-config         config-handler/get-app-config)
(def get-app-config-item    config-handler/get-app-config-item)
(def get-server-config      config-handler/get-server-config)
(def get-server-config-item config-handler/get-server-config-item)
(def get-site-config        config-handler/get-site-config)
(def get-site-config-item   config-handler/get-site-config-item)

; x.server-core.debug-handler
(def request->debug-mode debug-handler/request->debug-mode)

; x.server-core.engine
(def dom-value     engine/dom-value)
(def id            engine/id)
(def get-namespace engine/get-namespace)

; x.server-core.event-handler
(def event-vector?                    event-handler/event-vector?)
(def subscription-vector?             event-handler/subscription-vector?)
(def event-group-vector?              event-handler/event-group-vector?)
(def event-vector->param-vector       event-handler/event-vector->param-vector)
(def event-vector->event-id           event-handler/event-vector->event-id)
(def cofx->event-vector               event-handler/cofx->event-vector)
(def cofx->event-id                   event-handler/cofx->event-id)
(def context->event-vector            event-handler/context->event-vector)
(def context->event-id                event-handler/context->event-id)
(def context->db-before-effect        event-handler/context->db-before-effect)
(def context->db-after-effect         event-handler/context->db-after-effect)
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
(def ->interceptor                    event-handler/->interceptor)
(def inject-cofx                      event-handler/inject-cofx)
(def reg-cofx                         event-handler/reg-cofx)
(def reg-fx                           event-handler/reg-fx)
(def reg-sub                          event-handler/reg-sub)
(def self-destruct!                   event-handler/self-destruct!)
(def reg-event-db                     event-handler/reg-event-db)
(def reg-event-fx                     event-handler/reg-event-fx)
(def reg-handled-fx                   event-handler/reg-handled-fx)
(def dispatch                         event-handler/dispatch)
(def dispatch-sync                    event-handler/dispatch-sync)
(def dispatch-n                       event-handler/dispatch-n)
(def dispatch-if                      event-handler/dispatch-if)
(def dispatch-cond                    event-handler/dispatch-cond)
(def dispatch-tick                    event-handler/dispatch-tick)
(def subscribe                        event-handler/subscribe)
(def subscribed                       event-handler/subscribed)
(def r                                event-handler/r)

; x.server-core.lifecycle-handler
(def get-period-events lifecycle-handler/get-period-events)
(def reg-lifecycles    lifecycle-handler/reg-lifecycles)

; x.server-core.router-handler
(def route-template->route-match router-handler/route-template->route-match)
(def route-template->route-param router-handler/route-template->route-param)
(def request->route-match        router-handler/request->route-match)
(def request->route-param        router-handler/request->route-param)
