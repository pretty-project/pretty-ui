
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.20
; Description:
; Version: v0.4.0
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.debug
    (:require [mid-fruits.candy  :refer [param]]
              [mid-fruits.string :as string]
              [mid-fruits.time   :as time]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (ms)
(def DEBUG-STARTED (time/elapsed))



;; -- Separate event-stacks ---------------------------------------------------
;; ----------------------------------------------------------------------------

; A console könnyebb átláthatósága érdekében * ms-onként egy szeparátor
; üzenet-sorozatot ír a console-ra
;
; Mivel nincs szükség ujrarenderelésre, ezért nem szükséges reagent atom változót
; használni atom helyett

(def separated-at (atom DEBUG-STARTED))
(def separator-no (atom 0))

(defn- separate?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  []
  (let [elapsed-time (time/elapsed)]
       (> (- elapsed-time @separated-at)
          (param 2000))))

(defn- separate!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (?)
  []
  (let [elapsed-time (time/elapsed)]
       ; *
       (reset! separated-at elapsed-time)
       (swap!  separator-no inc)
       ; *
       (.log js/console (str "%c * Thin red line #" @separator-no " *") "color: red")))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn elapsed-time
  ; @return (integer)
  []
  (let [elapsed-time (time/elapsed)]
       (- elapsed-time DEBUG-STARTED)))

(defn elapsed-timestamp
  ; @return (string)
  []
  (str (str " | ")
       (Math/round (elapsed-time))
       (str "ms elapsed")))

(defn timestamp
  ; @return (string)
  []
  (string/bracket (str (time/get-hours) ":" (time/get-minutes) ":" (time/get-seconds)
                       (elapsed-timestamp))))

(defn console
  ; @param (string) group-label
  ; @param (*) n
  ;
  ; @return (?)
  ([n]
   (if (separate?)
       (separate!))
   (let [timestamp (timestamp)]
        (.log js/console (str timestamp string/break n))))

  ([group-label n]
   (if (separate?)
       (separate!))
   (let [timestamp (timestamp)]
        (.groupCollapsed js/console (str timestamp string/break group-label))
        (.log js/console (str n))
        (.groupEnd js/console))))
