
(ns elements.slider.prototypes
    (:require [elements.input.utils :as input.utils]
              [noop.api             :refer [param]]))

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
  ;  :value-path (Re-Frame path vector)}
  [slider-id slider-props]
  (merge {:max-value     100
          :min-value     0
          :initial-value [0 100]
          :value-path    (input.utils/default-value-path slider-id)
          :width         :auto}
         (param slider-props)))
