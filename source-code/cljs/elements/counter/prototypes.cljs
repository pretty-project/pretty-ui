
(ns elements.counter.prototypes
    (:require [elements.input.helpers :as input.helpers]
              [noop.api               :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn counter-props-prototype
  ; @ignore
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;
  ; @return (map)
  ; {:border-color (keyword or string)
  ;  :border-radius (map)
  ;  :border-position (keyword)
  ;  :border-width (keyword)
  ;  :font-size (keyword)
  ;  :initial-value (integer)
  ;  :value-path (vector)}
  [counter-id counter-props]
  (merge {:border-color    :default
          :border-position :all
          :border-radius   {:all :m}
          :border-width    :xs
          :font-size       :s
          :initial-value   0
          :value-path      (input.helpers/default-value-path counter-id)}
         (param counter-props)))
