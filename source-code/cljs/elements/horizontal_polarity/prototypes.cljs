
(ns elements.horizontal-polarity.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn polarity-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) polarity-props
  ;
  ; @return (map)
  ; {:vertical-align (keyword)}
  [polarity-props]
  (merge {:vertical-align :center}
         (param polarity-props)))
