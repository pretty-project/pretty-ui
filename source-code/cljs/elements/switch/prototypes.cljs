
(ns elements.switch.prototypes
    (:require [candy.api              :refer [param return]]
              [elements.input.helpers :as input.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn switch-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ;
  ; @return (map)
  ; {:border-color (keyword or string)
  ;  :border-radius (keyword)
  ;  :border-width (keyword)
  ;  :option-label-f (function)
  ;  :option-value-f (function)
  ;  :options-orientation (keyword)
  ;  :options-path (vector)
  ;  :value-path (vector)}
  [switch-id switch-props]
  (merge {:border-color        :default
          :border-radius       :m
          :border-width        :xs
          :option-label-f      return
          :option-value-f      return
          :options-orientation :vertical
          :options-path        (input.helpers/default-options-path switch-id)
          :value-path          (input.helpers/default-value-path   switch-id)}
         (param switch-props)))
