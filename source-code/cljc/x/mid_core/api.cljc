
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.08
; Description:
; Version: v0.3.8
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-core.api
    (:require [x.mid-core.cache-handler  :as cache-handler]
              [x.mid-core.config-handler :as config-handler]
              [x.mid-core.debug-handler  :as debug-handler]
              [x.mid-core.engine         :as engine]
              [x.mid-core.event-handler  :as event-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-core.cache-handler
(def cache-control-uri cache-handler/cache-control-uri)

; x.mid-core.config-handler
(def app-detail-path        config-handler/app-detail-path)
(def browser-detail-path    config-handler/browser-detail-path)
(def database-detail-path   config-handler/database-detail-path)
(def install-detail-path    config-handler/install-detail-path)
(def seo-detail-path        config-handler/seo-detail-path)
(def storage-detail-path    config-handler/storage-detail-path)
(def js-detail-path         config-handler/js-detail-path)
(def get-app-details        config-handler/get-app-details)
(def get-app-detail         config-handler/get-app-detail)
(def get-browser-details    config-handler/get-browser-details)
(def get-browser-detail     config-handler/get-browser-detail)
(def get-database-details   config-handler/get-database-details)
(def get-database-detail    config-handler/get-database-detail)
(def get-install-details    config-handler/get-install-details)
(def get-install-detail     config-handler/get-install-detail)
(def get-seo-details        config-handler/get-seo-details)
(def get-seo-detail         config-handler/get-seo-detail)
(def get-storage-details    config-handler/get-storage-details)
(def get-storage-detail     config-handler/get-storage-detail)
(def get-js-details         config-handler/get-js-details)
(def get-js-detail          config-handler/get-js-detail)
(def get-css-paths          config-handler/get-css-paths)
(def get-favicon-paths      config-handler/get-favicon-paths)
(def get-plugin-js-paths    config-handler/get-plugin-js-paths)
(def get-configs            config-handler/get-configs)
(def get-destructed-configs config-handler/get-destructed-configs)
(def get-config-item        config-handler/get-config-item)
(def store-configs!         config-handler/store-configs!)

; x.mid-core.debug-handler
(def query-string->debug-mode? debug-handler/query-string->debug-mode?)
(def query-string->debug-mode  debug-handler/query-string->debug-mode)

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
(def reg-prototype                    event-handler/reg-prototype)
(def use-prototype                    event-handler/use-prototype)
(def self-destruct!                   event-handler/self-destruct!)
(def get-event-handlers               event-handler/get-event-handlers)
(def get-event-handler                event-handler/get-event-handler)
(def event-handler-registrated?       event-handler/event-handler-registrated?)
(def reg-event-db                     event-handler/reg-event-db)
(def reg-event-fx                     event-handler/reg-event-fx)
(def reg-handled-fx                   event-handler/reg-handled-fx)
(def dispatch                         event-handler/dispatch)
(def dispatch-sync                    event-handler/dispatch-sync)
(def dispatch-n                       event-handler/dispatch-n)
(def dispatch-some                    event-handler/dispatch-some)
(def dispatch-if                      event-handler/dispatch-if)
(def dispatch-cond                    event-handler/dispatch-cond)
(def dispatch-tick                    event-handler/dispatch-tick)
(def subscribe                        event-handler/subscribe)
(def subscribed                       event-handler/subscribed)
(def r                                event-handler/r)