
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.x.core.debug-handler.helpers
    (:require [mid.x.core.debug-handler.config :as debug-handler.config]
              [string.api                      :as string]
              [vector.api                      :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn query-string->debug-mode
  ; @param (string) query-string
  ;
  ; @return (string)
  [query-string]
  (vector/first-filtered debug-handler.config/SAFEWORDS #(string/contains-part? query-string %)))
