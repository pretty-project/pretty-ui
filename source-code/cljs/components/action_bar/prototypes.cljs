
(ns components.action-bar.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bar-props-prototype
  ; @param (map) bar-props
  ;
  ; @return (map)
  [bar-props]
  (merge {}
         (param bar-props)))
