
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.08
; Description:
; Version: v0.5.2
; Compatibility: x4.5.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-core.api
    (:require [x.mid-core.debug-handler]
              [x.mid-core.build-handler  :as build-handler]
              [x.mid-core.cache-handler  :as cache-handler]
              [x.mid-core.config-handler :as config-handler]
              [x.mid-core.engine         :as engine]
              [x.mid-core.event-handler  :as event-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-core.build-handler
(def app-build build-handler/app-build)

; x.mid-core.cache-handler
(def cache-control-uri cache-handler/cache-control-uri)

; x.mid-core.config-handler
(def get-app-config         config-handler/get-app-config)
(def get-app-config-item    config-handler/get-app-config-item)
(def get-server-config      config-handler/get-server-config)
(def get-server-config-item config-handler/get-server-config-item)
(def get-site-config        config-handler/get-site-config)
(def get-site-config-item   config-handler/get-site-config-item)

; x.mid-core.engine
(def dom-value     engine/dom-value)
(def id            engine/id)
(def get-namespace engine/get-namespace)

; x.mid-core.event-handler
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
(def event-vector<-params             event-handler/event-vector<-params)
(def metamorphic-event<-params        event-handler/metamorphic-event<-params)
(def metamorphic-effects->effects-map event-handler/metamorphic-effects->effects-map)
(def merge-effects-maps               event-handler/merge-effects-maps)
(def ->interceptor                    event-handler/->interceptor)
(def inject-cofx                      event-handler/inject-cofx)
(def reg-cofx                         event-handler/reg-cofx)
(def reg-fx                           event-handler/reg-fx)
(def reg-sub                          event-handler/reg-sub)
(def event-vector<-id                 event-handler/event-vector<-id)
(def self-destruct!                   event-handler/self-destruct!)
(def debug!                           event-handler/debug!)
(def reg-event-db                     event-handler/reg-event-db)
(def reg-event-fx                     event-handler/reg-event-fx)
(def reg-handled-fx                   event-handler/reg-handled-fx)
(def dispatch                         event-handler/dispatch)
(def dispatch-sync                    event-handler/dispatch-sync)
(def dispatch-n                       event-handler/dispatch-n)
(def dispatch-later                   event-handler/dispatch-later)
(def dispatch-if                      event-handler/dispatch-if)
(def dispatch-cond                    event-handler/dispatch-cond)
(def dispatch-tick                    event-handler/dispatch-tick)
(def subscribe                        event-handler/subscribe)
(def subscribed                       event-handler/subscribed)
(def r                                event-handler/r)
