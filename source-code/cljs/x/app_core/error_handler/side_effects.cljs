

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.error-handler.side-effects
    (:require [x.app-core.event-handler :as event-handler]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn print-warning!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (list of strings) warning-message
  [& warning-message]
  (.warn js/console (reduce #(str %1 "\n" %2) nil warning-message)))

(defn print-error!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (list of strings) error-message
  [& error-message]
  (.error js/console (reduce #(str %1 "\n" %2) nil error-message)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(event-handler/reg-fx :core/print-warning! print-warning!)

; WARNING! NON-PUBLIC! DO NOT USE!
(event-handler/reg-fx :core/print-error! print-error!)
