

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns reagent.references
    (:require [reagent.core :as core]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn arguments
  ; @param (?) this
  ;
  ; @usage
  ;  (reagent/lifecycles {:component-did-update (fn [this] (reagent/arguments this))})
  ;
  ; @return (*)
  [this]
  (-> this core/argv rest))
