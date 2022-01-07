
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.01.07
; Description:
; Version: v0.3.2
; Compatibility: x4.5.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.print-handler
    (:require [mid-fruits.string :as string]
              [mid-fruits.time   :as time]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (ms)
(def APP-STARTED-AT (time/elapsed))

; @constant (ms)
(def SEPARATOR-DELAY 2000)



;; -- State -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (ms)
(def separated-at (atom APP-STARTED-AT))

; @atom (integer)
(def separator-no (atom 0))



;; -- Separate event-stacks ---------------------------------------------------
;; ----------------------------------------------------------------------------

; A console könnyebb átláthatósága érdekében * ms-onként egy szeparátor üzenetettel
; választja el az üzenetfolyamot

(defn- separate?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  []
  (-> (time/elapsed)
      (- @separated-at)
      (> SEPARATOR-DELAY)))

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
       (.log js/console (str "%c* Thin red line #" @separator-no " *") "color: red")))



;; -- Console functions -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn elapsed-time
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (integer)
  []
  (-> (time/elapsed)
      (- APP-STARTED-AT)))

(defn timestamp
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (string)
  []
  (-> (time/elapsed)
      (time/ms->time)
      (str " elapsed")
      (string/bracket)))

(defn console
  ; @param (string) group-label
  ; @param (*) n
  ;
  ; @return (?)
  [n]
  (let [timestamp (timestamp)]
       (if (separate?)
           (separate!))
       (if (-> n str (string/max-length? 60))
           ; If the message is shorter than 20 character ...
           (.log js/console (str timestamp string/break n))
           ; If the message is NOT shorter than 20 character ...
           (let [header (string/max-length n 60 "...")]
                (-> js/console (.groupCollapsed (str  "%c" timestamp string/break header) "font-weight: 400"))
                (-> js/console (.log            (str n)))
                (-> js/console (.groupEnd))))))
