
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.20
; Description:
; Version: v1.7.0
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.event-handler
    (:require [mid-fruits.candy         :refer [param return]]
              [mid-fruits.keyword       :as keyword]
              [mid-fruits.string        :as string]
              [mid-fruits.time          :as time]
              [mid-fruits.vector        :as vector]
              [re-frame.core            :as re-frame]
              [re-frame.registrar       :as registrar]
              [x.app-core.print-handler :as print-handler]
              [x.mid-core.event-handler :as event-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-core.event-handler
(def event-vector?              event-handler/event-vector?)
(def subscription-vector?       event-handler/subscription-vector?)
(def event-vector->param-vector event-handler/event-vector->param-vector)
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
(def dispatch-sync              event-handler/dispatch-sync)
(def dispatch-n                 event-handler/dispatch-n)
(def dispatch-later             event-handler/dispatch-later)
(def dispatch-if                event-handler/dispatch-if)
(def dispatch-cond              event-handler/dispatch-cond)
(def dispatch-tick              event-handler/dispatch-tick)
(def subscribe                  event-handler/subscribe)
(def subscribed                 event-handler/subscribed)
(def r                          event-handler/r)



;; -- State -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (map)
; A letiltott esemény-azonosítók és a tiltásuk feloldásainak idejei
(def EVENT-LOCKS (atom {}))



;; -- System interceptors -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn log-events?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; Az x.app-core.debug-handler és az x.app-core.event-handler körkörös függvőségben volna, ha ...
  [db]
  (boolean (if-let [debug-mode (get-in db [:core/debug-handler :meta-items :debug-mode])]
                   (= "pineapple-juice" debug-mode))))

; @constant (?)
(def LOG-EVENT! (->interceptor :id :core/log-event!
                               :before #(do (when (-> %1 context->db-before-effect log-events?)
                                                  (-> %1 context->event-vector     print-handler/console))
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



;; -- Low sample-rate dispatch functions --------------------------------------
;; ----------------------------------------------------------------------------

; Ritkított futású esemény-indítók: dispatch-last, dispatch-once
;
; Az (a/dispatch-last ...) és (a/dispatch-once ...) függvények kizárólag
; event-vector formátumban átadott eseményeket kezelnek, ugyanis más
; metamorphic-event formátumok nem rendelkeznek kizárólagos azonosítási
; lehetősséggel.

(defn- reg-event-lock
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) timeout
  ; @param (keyword) event-id
  ;
  ; @return (?)
  [timeout event-id]
  (let [elapsed-time (time/elapsed)
        unlock-time  (+ timeout elapsed-time)]
       (swap! EVENT-LOCKS assoc event-id unlock-time)))

(defn- event-unlocked?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) event-id
  ;
  ; @return (boolean)
  [event-id]
  (let [elapsed-time (time/elapsed)
        unlock-time  (get @EVENT-LOCKS event-id)]
       (> elapsed-time unlock-time)))

(defn- dispatch-unlocked?!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; Dispatch event if it is NOT locked
  ;
  ; @param (event-vector) event-vector
  ;
  ; @return (?)
  [event-vector]
  (let [event-id (event-vector->event-id event-vector)]
       (if (event-unlocked?   event-id)
           (re-frame/dispatch event-vector))))

(defn- delayed-try
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) timeout
  ; @param (event-vector) event-vector
  ;
  ; @return (?)
  [timeout event-vector]
  (let [event-id (event-vector->event-id event-vector)]
       (if (event-unlocked? event-id)
           (do (re-frame/dispatch event-vector)
               (reg-event-lock    timeout event-id)))))

(defn dispatch-last
  ; Blokkolja az esemény-meghívásokat mindaddig, amíg az utolsó esemény-meghívás
  ; után letelik a timeout. Ekkor az utolsó esemény-meghívást engedélyezi,
  ; az utolsó előttieket pedig figyelmen kívül hagyja.
  ;
  ; @param (integer) timeout
  ; @param (event-vector) event-vector
  ;
  ; @usage
  ;  (dispatch-last 500 [:foo-bar-baz])
  ;
  ; @return (?)
  [timeout event-vector]
  (let [event-id (event-vector->event-id event-vector)]
       (reg-event-lock    timeout event-id)
       (time/set-timeout! timeout #(dispatch-unlocked?! event-vector))))

(defn dispatch-once
  ; A megadott intervallumonként egy - az utolsó - esemény-meghívást engedélyezi,
  ; a többit figyelmen kívül hagyja.
  ;
  ; @param (integer) interval
  ; @param (event-vector) event-vector
  ;
  ; @usage
  ;  (dispatch-once 500 [:foo-bar-baz])
  ;
  ; @return (?)
  [interval event-vector]
  (let [event-id (event-vector->event-id event-vector)]
       (if (event-unlocked? event-id)
           (do (re-frame/dispatch event-vector)
               (reg-event-lock    interval event-id))
           (time/set-timeout! interval #(delayed-try interval event-vector)))))
