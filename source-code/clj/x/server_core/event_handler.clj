
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.14
; Description:
; Version: v1.5.2
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.event-handler
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.vector :as vector]
              [logger.api        :as logger]
              [re-frame.core     :as re-frame]
              [x.mid-core.event-handler :as event-handler]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keyword)
(def ERROR-EVENT-ID :core/->error-catched)

; @constant (string)
(def EVENT-LOG-FILENAME "x.server-events.log")



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
(def ->interceptor                    event-handler/->interceptor)
(def inject-cofx                      event-handler/inject-cofx)
(def reg-cofx                         event-handler/reg-cofx)
(def reg-fx                           event-handler/reg-fx)
(def reg-sub                          event-handler/reg-sub)
(def self-destruct!                   event-handler/self-destruct!)
(def get-event-handlers               event-handler/get-event-handlers)
(def get-event-handler                event-handler/get-event-handler)
(def event-handler-registrated?       event-handler/event-handler-registrated?)
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



;; -- System interceptors -----------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (?)
(def LOG-EVENT! (re-frame/->interceptor :id ::log-event!
                                        :before #(let [event-vector (context->event-vector %1)]
                                                     ; Szükséges korlátozni a fájl maximális méretét!
                                                     ;(logger/write! EVENT-LOG-FILENAME event-vector)
                                                      (return %1))))

; @constant (?)
(def CHECK-DB! (re-frame/->interceptor :id ::check-db!
                                       :after #(let [error-context (assoc %1 :error-event-id ERROR-EVENT-ID)
                                                     error-event   [ERROR-EVENT-ID (context->error-props %1)]]
                                                    (when (context->error-catched? error-context)
                                                          (re-frame/dispatch       error-event))
                                                    (return %1))))

(defn- interceptors<-system-interceptors
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (vector) interceptors
  ;
  ; @return (vector)
  [interceptors]
  (vector/conj-item interceptors LOG-EVENT!))
 ;(vector/conj-item interceptors LOG-EVENT! CHECK-DB!)



;; -- Event registrating ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-event-db
  ; @param (keyword) event-id
  ; @param (vector)(opt) interceptors
  ; @param (metamorphic-event) event-handler
  ([event-id event-handler]
   (reg-event-db event-id nil event-handler))

  ([event-id interceptors event-handler]
   (let [interceptors (interceptors<-system-interceptors interceptors)]
        (event-handler/reg-event-db event-id interceptors event-handler))))

(defn reg-event-fx
  ; @param (keyword) event-id
  ; @param (vector)(opt) interceptors
  ; @param (metamorphic-event) event-handler
  ([event-id event-handler]
   (reg-event-fx event-id nil event-handler))

  ([event-id interceptors event-handler]
   (let [interceptors (interceptors<-system-interceptors interceptors)]
        (event-handler/reg-event-fx event-id interceptors event-handler))))
