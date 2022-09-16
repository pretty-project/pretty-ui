
; WARNING! DEPRECATED! DO NOT USE!

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.event-handler
    (:require [mid-fruits.candy                      :refer [return]]
              [mid-fruits.vector                     :as vector]
              [re-frame.api                          :as re-frame]
              [x.app-core.print-handler.side-effects :as print-handler.side-effects]
              [x.mid-core.event-handler              :as event-handler]))



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
(def get-event-handlers         event-handler/get-event-handlers)
(def get-event-handler          event-handler/get-event-handler)
(def event-handler-registrated  event-handler/event-handler-registrated?)
(def reg-fx                     event-handler/reg-fx)
(def dispatch                   event-handler/dispatch)
(def dispatch-fx                event-handler/dispatch-fx)
(def dispatch-sync              event-handler/dispatch-sync)
(def dispatch-n                 event-handler/dispatch-n)
(def dispatch-later             event-handler/dispatch-later)
(def dispatch-if                event-handler/dispatch-if)
(def dispatch-cond              event-handler/dispatch-cond)
(def dispatch-tick              event-handler/dispatch-tick)
(def dispatch-last              re-frame/dispatch-last)
(def dispatch-once              re-frame/dispatch-once)
(def subscribe                  event-handler/subscribe)
(def subscribed                 event-handler/subscribed)
(def fx                         event-handler/fx)
(def fx-n                       event-handler/fx-n)
(def r                          event-handler/r)



;; -- System interceptors -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn log-events?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; Az x.app-core.debug-handler és az x.app-core.event-handler körkörös függvőségben volna, ha ...
  [db]
  (boolean (if-let [debug-mode (get-in db [:core :debug-handler/meta-items :debug-mode])]
                   (= "pineapple-juice" debug-mode))))

; @constant (?)
(def LOG-EVENT! (->interceptor :id :core/log-event!
                               :before #(do (when (-> %1 context->db-before-effect log-events?)
                                                  (-> %1 context->event-vector     print-handler.side-effects/console))
                                            (-> %1 return))))

(defn- interceptors<-system-interceptors
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (vector) interceptors
  ;
  ; @return (vector)
  [interceptors]
  (vector/conj-item interceptors LOG-EVENT!))



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
