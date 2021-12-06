
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.20
; Description:
; Version: v1.4.2
; Compatibility: x4.4.6



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
              [x.app-core.debug-handler :as debug-handler]
              [x.mid-core.event-handler :as event-handler]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (boolean)
(def LOG-EVENTS? (= (debug-handler/debug-mode)
                    (param "pineapple-juice")))



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
(def db                               event-handler/db)
(def ->interceptor                    event-handler/->interceptor)
(def inject-cofx                      event-handler/inject-cofx)
(def reg-cofx                         event-handler/reg-cofx)
(def reg-fx                           event-handler/reg-fx)
(def reg-sub                          event-handler/reg-sub)
(def get-event-handlers               event-handler/get-event-handlers)
(def get-event-handler                event-handler/get-event-handler)
(def event-handler-registrated?       event-handler/event-handler-registrated?)
(def reg-handled-fx                   event-handler/reg-handled-fx)
(def self-destruct!                   event-handler/self-destruct!)
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



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keyword)
(def ERROR-EVENT-ID :core/->error-catched)



;; -- State -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (map)
; A letiltott esemény-azonosítók és a tiltásuk feloldásainak idejei
(def EVENT-LOCKS (atom {}))



;; -- System interceptors -----------------------------------------------------
;; ----------------------------------------------------------------------------

(def log-event!
     (->interceptor
       :id ::log-event!
       :before ;(debug-handler/console (context->empty-event-vector %1)
               ;                       (string/join (context->event-props %1)
               ;                                    (str string/break string/break)))
               #(let [event-id        (context->event-id %1)
                      event-namespace (keyword/get-namespace event-id)]
                    ; TEMP
                    ; A collapsable event-log megvalósításáig az x.app-components.stated
                    ; névtér eseményei szűrésre kerülnek, a console átláthatóságának
                    ; érdekében.
                    (if-not (= event-namespace :components)
                            (debug-handler/console (context->event-vector %1)))
                           ;(debug-handler/console (context->event-vector %1)
                    (return %1))))

(def check-db!
     (->interceptor
       :id ::check-db!
       :after #(let [error-context (assoc %1 :error-event-id ERROR-EVENT-ID)
                     error-event   [ERROR-EVENT-ID (context->error-props %1)]]
                    (when (context->error-catched? error-context)
                          (dispatch error-event))
                    (return %1))))

(defn- interceptors<-system-interceptors
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (vector) interceptors
  ;
  ; @return (vector)
  [interceptors]
  (cond-> interceptors LOG-EVENTS? (vector/conj-item log-event!)
                       :always     (vector/conj-item check-db!)))



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



;; -- Dispatch functions ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn dispatch-later
  ; @param (maps in vector) event-list
  ;
  ; @usage
  ;  (dispatch-later [{:ms 500 :dispatch [:do-something!]}
  ;                   {:ms 600 :dispatch-n [[:do-something!]
  ;                                         [:do-something-else!]]}])
  [event-list]
  ; dispatch-f   instead dispatch:   To avoid name conflicts
  ; dispatch-n-f instead dispatch-n: To avoid name conflicts
  (let [dispatch-f   dispatch
        dispatch-n-f dispatch-n]
       (doseq [{:keys [ms dispatch dispatch-n]} (remove nil? event-list)]
              (cond (and (some? dispatch)
                         (number? ms))
                    (time/set-timeout! ms #(dispatch-f dispatch))
                    (and (some? dispatch-n)
                         (number? ms))
                    (time/set-timeout! ms #(dispatch-n-f dispatch-n))))))

(registrar/clear-handlers :fx :dispatch-later)
(re-frame/reg-fx :dispatch-later dispatch-later)



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
