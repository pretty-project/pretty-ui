
(ns elements.switch.prototypes
    (:require [elements.input.helpers :as input.helpers]
              [noop.api               :refer [param return]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn switch-props-prototype
  ; @ignore
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
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
  [switch-id switch-props]
  (merge {:border-color        :default
          :border-position     :all
          :border-radius       {:all :m}
          :border-width        :xs
          :font-size           :s
          :option-label-f      return
          :option-value-f      return
          :options-orientation :vertical
          :options-path        (input.helpers/default-options-path switch-id)
          :value-path          (input.helpers/default-value-path   switch-id)}
         (param switch-props)))
