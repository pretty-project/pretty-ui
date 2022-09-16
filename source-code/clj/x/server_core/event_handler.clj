
; WARNING! DEPRECATED! DO NOT USE!

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.event-handler
    (:require [x.mid-core.event-handler :as event-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-core.event-handler
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
(def get-event-handlers         event-handler/get-event-handlers)
(def get-event-handler          event-handler/get-event-handler)
(def event-handler-registrated? event-handler/event-handler-registrated?)
(def reg-fx                     event-handler/reg-fx)
(def dispatch                   event-handler/dispatch)
(def dispatch-fx                event-handler/dispatch-fx)
(def dispatch-sync              event-handler/dispatch-sync)
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
