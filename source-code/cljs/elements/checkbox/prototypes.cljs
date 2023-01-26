
(ns elements.checkbox.prototypes
    (:require [noop.api             :refer [param return]]
              [elements.input.utils :as input.utils]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn checkbox-props-prototype
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ;
  ; @return (map)
  ; {:border-color (keyword or string)
  ;  :border-position (keyword)
  ;  :border-radius (map)
  ;  :border-width (keyword)
  ;  :font-size (keyword)
  ;  :option-label-f (function)
  ;  :option-value-f (function)
  ;  :options-orientation (keyword)
  ;  :options-path (vector)
  ;  :value-path (vector)}
  [checkbox-id checkbox-props]
  (merge {:border-color        :default
          :border-position     :all
          :border-radius       {:all :xs}
          :border-width        :xs
          :font-size           :s
          :option-label-f      return
          :option-value-f      return
          :options-orientation :vertical
          :options-path        (input.utils/default-options-path checkbox-id)
          :value-path          (input.utils/default-value-path   checkbox-id)}
         (param checkbox-props)))
