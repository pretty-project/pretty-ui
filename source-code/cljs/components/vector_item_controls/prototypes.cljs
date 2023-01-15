
(ns components.vector-item-controls.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn controls-props-prototype
  ; @param (map) controls-props
  ;
  ; @return (map)
  ; {}
  [controls-props]
  (merge {:tooltip-position :right}
         (param controls-props)))
