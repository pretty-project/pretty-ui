
(ns elements.blank.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn blank-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) blank-props
  ;
  ; @return (map)
  ; {}
  [blank-props]
  (merge {}
         (param blank-props)))
