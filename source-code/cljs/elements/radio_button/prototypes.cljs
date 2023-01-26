
(ns elements.radio-button.prototypes
    (:require [elements.input.utils :as input.utils]
              [noop.api             :refer [param return]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
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
  [button-id button-props]
  (merge {:border-color        :default
          :border-position     :all
          :border-radius       {:all :m}
          :border-width        :xs
          :font-size           :s
          :options-orientation :vertical
          :options-path        (input.utils/default-options-path button-id)
          :value-path          (input.utils/default-value-path   button-id)
          :option-label-f      return
          :option-value-f      return}
         (param button-props)))
