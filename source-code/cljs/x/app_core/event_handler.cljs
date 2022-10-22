
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
              [x.app-core.print-handler.side-effects :as print-handler.side-effects]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-core.event-handler
(def event-vector?              re-frame/event-vector?)
(def subscription-vector?       re-frame/subscription-vector?)
(def event-vector->event-id     re-frame/event-vector->event-id)
(def cofx->event-vector         re-frame/cofx->event-vector)
(def cofx->event-id             re-frame/cofx->event-id)
(def context->event-vector      re-frame/context->event-vector)
(def context->event-id          re-frame/context->event-id)
(def context->db-before-effect  re-frame/context->db-before-effect)
(def context->db-after-effect   re-frame/context->db-after-effect)
(def event-vector<-params       re-frame/event-vector<-params)
(def metamorphic-event<-params  re-frame/metamorphic-event<-params)
(def merge-effects-maps         re-frame/merge-effects-maps)
(def ->interceptor              re-frame/->interceptor)
(def inject-cofx                re-frame/inject-cofx)
(def reg-cofx                   re-frame/reg-cofx)
(def reg-sub                    re-frame/reg-sub)
(def event-vector<-id           re-frame/event-vector<-id)
(def debug!                     re-frame/debug!)
(def get-event-handlers         re-frame/get-event-handlers)
(def get-event-handler          re-frame/get-event-handler)
(def event-handler-registrated  re-frame/event-handler-registrated?)
(def reg-fx                     re-frame/reg-fx)
(def dispatch                   re-frame/dispatch)
(def dispatch-fx                re-frame/dispatch-fx)
(def dispatch-sync              re-frame/dispatch-sync)
(def dispatch-n                 re-frame/dispatch-n)
(def dispatch-later             re-frame/dispatch-later)
(def dispatch-if                re-frame/dispatch-if)
(def dispatch-cond              re-frame/dispatch-cond)
(def dispatch-tick              re-frame/dispatch-tick)
(def dispatch-last              re-frame/dispatch-last)
(def dispatch-once              re-frame/dispatch-once)
(def subscribe                  re-frame/subscribe)
(def subscribed                 re-frame/subscribed)
(def fx                         re-frame/fx)
(def fx-n                       re-frame/fx-n)
(def r                          re-frame/r)



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
        (re-frame/reg-event-db event-id interceptors event-handler))))

(defn reg-event-fx
  ; @param (keyword) event-id
  ; @param (vector)(opt) interceptors
  ; @param (metamorphic-event) event-handler
  ([event-id event-handler]
   (reg-event-fx event-id nil event-handler))

  ([event-id interceptors event-handler]
   (let [interceptors (interceptors<-system-interceptors interceptors)]
        (re-frame/reg-event-fx event-id interceptors event-handler))))
