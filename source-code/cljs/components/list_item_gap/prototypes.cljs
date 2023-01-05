
(ns components.list-item-gap.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn gap-props-prototype
  ; @param (map) row-props
  ;
  ; @return (map)
  [gap-props]
  (merge {}
         (param gap-props)))
