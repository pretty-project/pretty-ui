
(ns elements.point-diagram.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) diagram-props
  ;
  ; @return (map)
  ; {}
  [diagram-props]
  (merge {}
         (param diagram-props)))
