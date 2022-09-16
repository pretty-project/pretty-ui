
; WARNING! DEPRECATED! DO NOT USE!

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-core.event-handler
    (:require [mid.re-frame.api :as mid.re-frame.api]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.re-frame.api
(def ->interceptor                  mid.re-frame.api/->interceptor)
(def reg-cofx                       mid.re-frame.api/reg-cofx)
(def reg-sub                        mid.re-frame.api/reg-sub)
(def subscribe                      mid.re-frame.api/subscribe)
(def inject-cofx                    mid.re-frame.api/inject-cofx)
(def reg-event-db                   mid.re-frame.api/reg-event-db)
(def reg-event-fx                   mid.re-frame.api/reg-event-fx)
(def apply-fx-params                mid.re-frame.api/apply-fx-params)
(def reg-fx                         mid.re-frame.api/reg-fx)
(def event-vector?                  mid.re-frame.api/event-vector?)
(def subscription-vector?           mid.re-frame.api/subscription-vector?)
(def event-vector->event-id         mid.re-frame.api/event-vector->event-id)
(def cofx->event-vector             mid.re-frame.api/cofx->event-vector)
(def cofx->event-id                 mid.re-frame.api/cofx->event-id)
(def context->event-vector          mid.re-frame.api/context->event-vector)
(def context->event-id              mid.re-frame.api/context->event-id)
(def context->db-before-effect      mid.re-frame.api/context->db-before-effect)
(def context->db-after-effect       mid.re-frame.api/context->db-after-effect)
(def event-vector<-params           mid.re-frame.api/event-vector<-params)
(def metamorphic-event<-params      mid.re-frame.api/metamorphic-event<-params)
(def event-vector->effects-map      mid.re-frame.api/event-vector->effects-map)
(def event-vector->handler-f        mid.re-frame.api/event-vector->handler-f)
(def effects-map->handler-f         mid.re-frame.api/effects-map->handler-f)
(def metamorphic-handler->handler-f mid.re-frame.api/metamorphic-handler->handler-f)
(def metamorphic-event->effects-map mid.re-frame.api/metamorphic-event->effects-map)
(def effects-map<-event             mid.re-frame.api/effects-map<-event)
(def merge-effects-maps             mid.re-frame.api/merge-effects-maps)
(def event-vector<-id-f             mid.re-frame.api/event-vector<-id-f)
(def event-vector<-id               mid.re-frame.api/event-vector<-id)
(def get-event-handlers             mid.re-frame.api/get-event-handlers)
(def get-event-handler              mid.re-frame.api/get-event-handler)
(def event-handler-registrated?     mid.re-frame.api/event-handler-registrated?)
(def debug!                         mid.re-frame.api/debug!)
(def dispatch                       mid.re-frame.api/dispatch)
(def dispatch-fx                    mid.re-frame.api/dispatch-fx)
(def dispatch-sync                  mid.re-frame.api/dispatch-sync)
(def dispatch-n                     mid.re-frame.api/dispatch-n)
(def dispatch-later                 mid.re-frame.api/dispatch-later)
(def dispatch-if                    mid.re-frame.api/dispatch-if)
(def dispatch-cond                  mid.re-frame.api/dispatch-cond)
(def subscribed                     mid.re-frame.api/subscribed)
(def fx                             mid.re-frame.api/fx)
(def fx-n                           mid.re-frame.api/fx-n)
(def dispatch-tick                  mid.re-frame.api/dispatch-tick)
(def r                              mid.re-frame.api/r)
