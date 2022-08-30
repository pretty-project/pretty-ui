
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.re-frame.api
    (:require [mid.re-frame.cofx          :as cofx]
              [mid.re-frame.core          :as core]
              [mid.re-frame.context       :as context]
              [mid.re-frame.debug         :as debug]
              [mid.re-frame.dispatch      :as dispatch]
              [mid.re-frame.effects-map   :as effects-map]
              [mid.re-frame.event-handler :as event-handler]
              [mid.re-frame.event-vector  :as event-vector]
              [mid.re-frame.fx            :as fx]
              [mid.re-frame.id            :as id]
              [mid.re-frame.metamorphic   :as metamorphic]
              [mid.re-frame.reg           :as reg]
              [mid.re-frame.tick          :as tick]
              [mid.re-frame.types         :as types]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.re-frame.cofx
(def cofx->event-vector cofx/cofx->event-vector)
(def cofx->event-id     cofx/cofx->event-id)

; mid.re-frame.core
(def subscribe     core/subscribe)
(def subscribed    core/subscribed)
(def ->interceptor core/->interceptor)
(def inject-cofx   core/inject-cofx)
(def r             core/r)

; mid.re-frame.context
(def context->event-vector     context/context->event-vector)
(def context->event-id         context/context->event-id)
(def context->db-before-effect context/context->db-before-effect)
(def context->db-after-effect  context/context->db-after-effect)

; mid.re-frame.debug
(def debug! debug/debug!)

; mid.re-frame.dispatch
(def dispatch       dispatch/dispatch)
(def dispatch-sync  dispatch/dispatch-sync)
(def dispatch-n     dispatch/dispatch-n)
(def dispatch-later dispatch/dispatch-later)
(def dispatch-if    dispatch/dispatch-if)
(def dispatch-cond  dispatch/dispatch-cond)

; mid.re-frame.effects-map
(def effects-map<-event     effects-map/effects-map<-event)
(def merge-effects-maps     effects-map/merge-effects-maps)
(def effects-map->handler-f effects-map/effects-map->handler-f)

; mid.re-frame.event-handler
(def get-event-handlers         event-handler/get-event-handlers)
(def get-event-handler          event-handler/get-event-handler)
(def event-handler-registrated? event-handler/event-handler-registrated?)

; mid.re-frame.event-vector
(def event-vector->event-id    event-vector/event-vector->event-id)
(def event-vector->effects-map event-vector/event-vector->effects-map)
(def event-vector->handler-f   event-vector/event-vector->handler-f)
(def event-vector<-params      event-vector/event-vector<-params)

; mid.re-frame.fx
(def fx   fx/fx)
(def fx-n fx/fx-n)

; mid.re-frame.id
(def event-vector<-id-f id/event-vector<-id-f)
(def event-vector<-id   id/event-vector<-id)

; mid.re-frame.metamorphic
(def metamorphic-handler->handler-f metamorphic/metamorphic-handler->handler-f)
(def metamorphic-event->effects-map metamorphic/metamorphic-event->effects-map)
(def metamorphic-event<-params      metamorphic/metamorphic-event<-params)

; mid.re-frame.reg
(def reg-cofx        reg/reg-cofx)
(def reg-sub         reg/reg-sub)
(def reg-event-db    reg/reg-event-db)
(def reg-event-fx    reg/reg-event-fx)
(def apply-fx-params reg/apply-fx-params)
(def reg-fx          reg/reg-fx)

; mid.re-frame.tick
(def dispatch-tick tick/dispatch-tick)

; mid.re-frame.types
(def event-vector?        types/event-vector?)
(def subscription-vector? types/subscription-vector?)
