
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
