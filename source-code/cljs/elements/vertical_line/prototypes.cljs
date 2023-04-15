
(ns elements.vertical-line.prototypes
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
  ;  :height (keyword)
  ;  :strength (px)}
  [line-props]
  (merge {:fill-color :highlight
          :height     :parent
          :strength   1}
         (param line-props)))
