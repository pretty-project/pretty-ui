
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.16
; Description:
; Version: v0.2.6
; Compatibility: x4.4.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-core.debug-handler
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.loop   :refer [reduce-while]]
              [mid-fruits.string :as string]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (vector)
(def SAFEWORDS ["pineapple-juice" "avocado-juice"])



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn query-string->debug-mode?
  ; @param (string) query-string
  ;
  ; @return (boolean)
  [query-string]
  (reduce-while (fn [_ %2] (string/contains-part? query-string %2))
                (param false)
                (param SAFEWORDS)
                (fn [%1 _] (true? %1))))

(defn query-string->debug-mode
  ; @param (string) query-string
  ;
  ; @return (string)
  [query-string]
  (reduce-while (fn [_ %2] (if (string/contains-part? query-string %2)
                               (return %2)))
                (param nil)
                (param SAFEWORDS)
                (fn [%1 _] (some? %1))))
