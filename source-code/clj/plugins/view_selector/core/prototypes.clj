
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.core.prototypes
    (:require [mid-fruits.candy :refer [param]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn selector-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;
  ; @return (map)
  [selector-id selector-props]
  (merge {}
         (param selector-props)))
