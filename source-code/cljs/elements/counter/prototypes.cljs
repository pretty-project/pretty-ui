
(ns elements.counter.prototypes
    (:require [candy.api              :refer [param]]
              [elements.input.helpers :as input.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn counter-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;
  ; @return (map)
  ; {:border-color (keyword or string)
  ;  :border-radius (keyword)
  ;  :border-position (keyword)
  ;  :border-width (keyword)
  ;  :font-size (keyword)
  ;  :initial-value (integer)
  ;  :value-path (vector)}
  [counter-id counter-props]
  (merge {:border-color    :default
          :border-position :all
          :border-radius   :m
          :border-width    :xs
          :font-size       :s
          :initial-value   0
          :value-path      (input.helpers/default-value-path counter-id)}
         (param counter-props)))
