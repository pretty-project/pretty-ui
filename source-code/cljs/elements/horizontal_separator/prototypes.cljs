
(ns elements.horizontal-separator.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn separator-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) separator-props
  ;
  ; @return (map)
  ; {:height (keyword)}
  [separator-props]
  (merge {:height :s}
         (param separator-props)))
