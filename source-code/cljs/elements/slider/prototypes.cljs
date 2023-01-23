
(ns elements.slider.prototypes
    (:require [elements.input.helpers :as input.helpers]
              [noop.api               :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn slider-props-prototype
  ; @ignore
  ;
  ; @param (keyword) slider-id
  ; @param (map) slider-props
  ;
  ; @return (map)
  ; {:max-value (integer)
  ;  :min-value (integer)
  ;  :initial-value (vector)
  ;  :value-path (vector)}
  [slider-id slider-props]
  (merge {:max-value     100
          :min-value     0
          :initial-value [0 100]
          :value-path    (input.helpers/default-value-path slider-id)}
         (param slider-props)))
