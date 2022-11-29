
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.reagent.references
    (:require [reagent.core :as core]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn arguments
  ; @param (?) this
  ;
  ; @usage
  ;  (lifecycles {:component-did-update (fn [this] (arguments this))})
  ;
  ; @return (*)
  [this]
  (-> this core/argv rest))
