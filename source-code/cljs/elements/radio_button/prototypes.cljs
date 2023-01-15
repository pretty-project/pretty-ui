
(ns elements.radio-button.prototypes
    (:require [candy.api              :refer [param return]]
              [elements.input.helpers :as input.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {:border-color (keyword or string)
  ;  :border-position (keyword)
  ;  :border-radius (keyword)
  ;  :border-width (keyword)
  ;  :font-size (keyword)
  ;  :option-label-f (function)
  ;  :option-value-f (function)
  ;  :options-orientation (keyword)
  ;  :options-path (vector)
  ;  :value-path (vector)}
  [button-id button-props]
  (merge {:border-color        :default
          :border-position     :all
          :border-radius       :m
          :border-width        :xs
          :font-size           :s
          :options-orientation :vertical
          :options-path        (input.helpers/default-options-path button-id)
          :value-path          (input.helpers/default-value-path   button-id)
          :option-label-f      return
          :option-value-f      return}
         (param button-props)))
