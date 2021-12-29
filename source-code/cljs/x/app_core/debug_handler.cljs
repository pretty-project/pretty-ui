
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.16
; Description:
; Version: v0.3.2
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.debug-handler
    (:require [app-fruits.window        :as window]
              [mid-fruits.candy         :refer [param return]]
              [mid-fruits.string        :as string]
              [mid-fruits.time          :as time]
              [mid-fruits.uri           :as uri]
              [x.mid-core.debug-handler :as debug-handler]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Az x.app-core.debug-handler névtérben nem lehetséges Re-Frame eseményeket regisztrálni
; Az x.app-core.debug-handler és x.app-core.event-handler névterek körkörös függőségben vannak.



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def query-string->debug-mode? debug-handler/query-string->debug-mode?)
(def query-string->debug-mode  debug-handler/query-string->debug-mode)



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (ms)
(def DEBUG-STARTED (time/elapsed))

; @constant (ms)
(def SEPARATOR-DELAY 2000)



;; -- State -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (ms)
(def separated-at (atom DEBUG-STARTED))

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
      (- DEBUG-STARTED)))

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



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn debug-mode?
  ; @return (boolean)
  []
  (let [uri          (window/get-uri)
        query-string (uri/uri->query-string uri)]
       (query-string->debug-mode? query-string)))

(defn debug-mode
  ; @return (string)
  []
  (let [uri          (window/get-uri)
        query-string (uri/uri->query-string uri)]
       (query-string->debug-mode query-string)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-debug-mode
  ; @return (string)
  [db _]
  (get-in db [::primary :meta-items :debug-mode]))

(defn debug-mode-detected?
  ; @return (boolean)
  [db [event-id]]
  (let [debug-mode (get-debug-mode db [event-id])]
       (some? debug-mode)))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- store-debug-mode!
  ; @param (string) debug-mode
  ;
  ; @return (map)
  [db [_ debug-mode]]
  (assoc-in db [::primary :meta-items :debug-mode] debug-mode))