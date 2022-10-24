
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.namespace
    (:require [mid-fruits.keyword :as keyword]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn detect
  ; @param (namespaced keyword) sample
  ;
  ; @usage
  ;  (detect ::this)
  ;
  ; @return (keyword)
  [sample]
  (keyword/get-namespace sample))
