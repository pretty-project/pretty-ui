
(ns pretty-inputs.radio-button.prototypes
    (:require [fruits.noop.api             :refer [return]]
              [pretty-inputs.input.utils :as input.utils]
              [pretty-build-kit.api :as pretty-build-kit]))

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
  ;  :border-width (keyword, px or string)
  ;  :click-effect (keyword)
  ;  :font-size (keyword, px or string)
  ;  :option-label-f (function)
  ;  :option-value-f (function)
  ;  :options-orientation (keyword)
  ;  :options-path (Re-Frame path vector)
  ;  :value-path (Re-Frame path vector)}
  [button-id button-props]
  (merge {:border-color        :default
          :border-position     :all
          :border-radius       {:all :m}
          :border-width        :xs
          :click-effect        :opacity
          :font-size           :s
          :options-orientation :vertical
          :options-path        (input.utils/default-options-path button-id)
          :value-path          (input.utils/default-value-path   button-id)
          :option-label-f      return
          :option-value-f      return}
         (-> button-props)))
