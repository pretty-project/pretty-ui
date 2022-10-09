
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.error-handler.side-effects
    (:require [re-frame.api  :as r]
              [x.app-details :as details]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn print-warning!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (list of strings) warning-message
  [& warning-message]
  (println (reduce #(str %1 "\n" %2) nil warning-message)))

(defn print-error!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (list of strings) error-message
  [& error-message]
  (println (reduce #(str %1 "\n" %2) nil error-message)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :core/print-warning! print-warning!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :core/print-error! print-error!)
