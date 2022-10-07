
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns re-frame.dispatch
    (:require [mid.re-frame.dispatch :as dispatch]
              [re-frame.core         :as core]
              [re-frame.event-vector :as event-vector]
              [re-frame.state        :as state]
              [time.api              :as time]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.re-frame.dispatch
(def dispatch       dispatch/dispatch)
(def dispatch-fx    dispatch/dispatch-fx)
(def dispatch-sync  dispatch/dispatch-sync)
(def dispatch-n     dispatch/dispatch-n)
(def dispatch-later dispatch/dispatch-later)
(def dispatch-if    dispatch/dispatch-if)
(def dispatch-cond  dispatch/dispatch-cond)



;; -- Low sample-rate dispatch functions --------------------------------------
;; ----------------------------------------------------------------------------

; Ritkított futású esemény-indítók: dispatch-last, dispatch-once
;
; Az dispatch-last és dispatch-once függvények kizárólag event-vector
; formátumban átadott eseményeket kezelnek, ugyanis más metamorphic-event
; formátumok nem rendelkeznek kizárólagos azonosítási lehetősséggel.

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
       (swap! state/EVENT-LOCKS assoc event-id unlock-time)))

(defn- event-unlocked?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) event-id
  ;
  ; @return (boolean)
  [event-id]
  (let [elapsed-time (time/elapsed)
        unlock-time  (get @state/EVENT-LOCKS event-id)]
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
  (let [event-id (event-vector/event-vector->event-id event-vector)]
       (if (event-unlocked? event-id)
           (core/dispatch   event-vector))))

(defn- delayed-try
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) timeout
  ; @param (event-vector) event-vector
  ;
  ; @return (?)
  [timeout event-vector]
  (let [event-id (event-vector/event-vector->event-id event-vector)]
       (if (event-unlocked? event-id)
           (do (core/dispatch  event-vector)
               (reg-event-lock timeout event-id)))))

(defn dispatch-last
  ; Blokkolja az esemény-meghívásokat mindaddig, amíg az utolsó esemény-meghívás
  ; után letelik a timeout. Ekkor az utolsó esemény-meghívást engedélyezi,
  ; az utolsó előttieket pedig figyelmen kívül hagyja.
  ;
  ; @param (integer) timeout
  ; @param (event-vector) event-vector
  ;
  ; @usage
  ;  (e-frame/dispatch-last 500 [:foo-bar-baz])
  ;
  ; @return (?)
  [timeout event-vector]
  (let [event-id (event-vector/event-vector->event-id event-vector)]
       (reg-event-lock    timeout event-id)
       (letfn [(f [] (dispatch-unlocked?! event-vector))]
              (time/set-timeout! f timeout))))

(defn dispatch-once
  ; A megadott intervallumonként egy - az utolsó - esemény-meghívást engedélyezi,
  ; a többit figyelmen kívül hagyja.
  ;
  ; @param (integer) interval
  ; @param (event-vector) event-vector
  ;
  ; @usage
  ;  (re-frame/dispatch-once 500 [:foo-bar-baz])
  ;
  ; @return (?)
  [interval event-vector]
  (let [event-id (event-vector/event-vector->event-id event-vector)]
       (if (event-unlocked? event-id)
           (do (core/dispatch  event-vector)
               (reg-event-lock interval event-id))
           (letfn [(f [] (delayed-try interval event-vector))]
                  (time/set-timeout! f interval)))))
