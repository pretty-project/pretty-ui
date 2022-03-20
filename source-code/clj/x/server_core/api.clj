
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.api
    (:require [x.server-core.build-handler.transfer]
              [x.server-core.config-handler.side-effects]
              [x.server-core.config-handler.transfer]
              [x.server-core.database-handler.effects]
              [x.server-core.error-handler.side-effects]
              [x.server-core.resource-handler.events]
              [x.server-core.resource-handler.lifecycles]
              [x.server-core.resource-handler.subs]
              [x.server-core.server-handler.events]
              [x.server-core.server-handler.side-effects]
              [x.server-core.transfer-handler.lifecycles]
              [x.server-core.build-handler.side-effects     :as build-handler.side-effects]
              [x.server-core.build-handler.subs             :as build-handler.subs]
              [x.server-core.cache-handler.helpers          :as cache-handler.helpers]
              [x.server-core.config-handler.config          :as config-handler.config]
              [x.server-core.config-handler.subs            :as config-handler.subs]
              [x.server-core.debug-handler.helpers          :as debug-handler.helpers]
              [x.server-core.engine                         :as engine]
              [x.server-core.event-handler                  :as event-handler]
              [x.server-core.lifecycle-handler.side-effects :as lifecycle-handler.side-effects]
              [x.server-core.lifecycle-handler.subs         :as lifecycle-handler.subs]
              [x.server-core.transfer-handler.side-effects  :as transfer-handler.side-effects]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.server-core.build-handler.side-effects
(def app-build   build-handler.side-effects/app-build)
(def ->app-built build-handler.side-effects/->app-built)

; x.server-core.build-handler.subs
(def get-app-build build-handler.subs/get-app-build)

; x.server-core.cache-handler.helpers
(def request->app-cached? cache-handler.helpers/request->app-cached?)
(def cache-control-uri    cache-handler.helpers/cache-control-uri)

; x.server-core.config-handler.config
(def APP-CONFIG-FILEPATH    config-handler.config/APP-CONFIG-FILEPATH)
(def SITE-CONFIG-FILEPATH   config-handler.config/SITE-CONFIG-FILEPATH)
(def SERVER-CONFIG-FILEPATH config-handler.config/SERVER-CONFIG-FILEPATH)

; x.server-core.config-handler.subs
(def get-app-config         config-handler.subs/get-app-config)
(def get-app-config-item    config-handler.subs/get-app-config-item)
(def get-server-config      config-handler.subs/get-server-config)
(def get-server-config-item config-handler.subs/get-server-config-item)
(def get-site-config        config-handler.subs/get-site-config)
(def get-site-config-item   config-handler.subs/get-site-config-item)

; x.server-core.debug-handler.helpers
(def request->debug-mode debug-handler.helpers/request->debug-mode)

; x.server-core.engine
(def dom-value     engine/dom-value)
(def id            engine/id)
(def get-namespace engine/get-namespace)

; x.server-core.event-handler
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
(def dispatch-n                 event-handler/dispatch-n)
(def dispatch-later             event-handler/dispatch-later)
(def dispatch-if                event-handler/dispatch-if)
(def dispatch-cond              event-handler/dispatch-cond)
(def dispatch-tick              event-handler/dispatch-tick)
(def subscribe                  event-handler/subscribe)
(def subscribed                 event-handler/subscribed)
(def r                          event-handler/r)

; x.server-core.lifecycle-handler.side-effects
(def reg-lifecycles! lifecycle-handler.side-effects/reg-lifecycles!)

; x.server-core.lifecycle-handler.subs
(def get-period-events lifecycle-handler.subs/get-period-events)

; x.server-core.transfer-handler.side-effects
(def reg-transfer! transfer-handler.side-effects/reg-transfer!)
