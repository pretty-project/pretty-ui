
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.16
; Description:
; Version: v0.4.0
; Compatibility: x4.5.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-core.debug-handler
    (:require [mid-fruits.string :as string]
              [mid-fruits.vector :as vector]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (strings in vector)
(def SAFEWORDS ["pineapple-juice" "avocado-juice"])



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn query-string->debug-mode
  ; @param (string) query-string
  ;
  ; @return (string)
  [query-string]
  (vector/first-filtered SAFEWORDS #(string/contains-part? query-string %)))
