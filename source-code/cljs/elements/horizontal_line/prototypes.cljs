
(ns elements.horizontal-line.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn line-props-prototype
  ; @ignore
  ;
  ; @param (map) line-props
  ;
  ; @return (map)
  ; {:fill-color (keyword or string)
  ;  :strength (px)}
  [line-props]
  (merge {:fill-color :highlight
          :strength   1}
         (param line-props)))
