
(ns elements.stepper.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn stepper-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) stepper-props
  ;
  ; @return (map)
  ; {}
  [stepper-props]
  (merge {}
         (param stepper-props)))
