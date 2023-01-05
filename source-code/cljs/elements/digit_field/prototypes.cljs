
(ns elements.digit-field.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) field-props
  ;
  ; @return (map)
  ; {}
  [field-props]
  (merge {}
         (param field-props)))
