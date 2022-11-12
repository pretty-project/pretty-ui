
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.print-handler.side-effects
    (:require [mid-fruits.string               :as string]
              [syntax.api                      :as syntax]
              [time.api                        :as time]
              [x.app-core.print-handler.config :as print-handler.config]
              [x.app-core.print-handler.state  :as print-handler.state]))



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
      (- @print-handler.state/SEPARATED-AT)
      (> print-handler.config/SEPARATOR-DELAY)))

(defn- separate!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (?)
  []
  (let [elapsed-time (time/elapsed)]
       ; *
       (reset! print-handler.state/SEPARATED-AT elapsed-time)
       (swap!  print-handler.state/SEPARATOR-NO inc)
       ; *
       (.log js/console (str "%c* Thin red line #" @print-handler.state/SEPARATOR-NO " *") "color: red")))



;; -- Console functions -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn elapsed-time
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (integer)
  []
  (-> (time/elapsed)
      (- print-handler.config/APP-STARTED-AT)))

(defn timestamp
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (string)
  []
  (-> (time/elapsed)
      (time/ms->time)
      (str " elapsed")
      (syntax/bracket)))

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
