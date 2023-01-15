
(ns components.illustration.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn illustration-props-prototype
  ; @param (map) illustration-props
  ;
  ; @return (map)
  ; {:height (keyword)
  ;  :width (keyword)}
  [illustration-props]
  (merge {:height   :xxl
          :width    :xxl}
         (param illustration-props)))
