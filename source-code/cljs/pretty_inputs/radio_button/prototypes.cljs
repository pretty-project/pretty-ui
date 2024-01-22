
(ns pretty-inputs.radio-button.prototypes
    (:require [fruits.noop.api           :refer [none return]]
              [pretty-build-kit.api      :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; @ignore
  ;
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {:border-color (keyword or string)
  ;  :border-position (keyword)
  ;  :border-radius (map)
  ;  :border-width (keyword, px or string)
  ;  :click-effect (keyword)
  ;  :font-size (keyword, px or string)
  ;  :max-selection (integer)
  ;  :option-helper-f (function)
  ;  :option-label-f (function)
  ;  :option-value-f (function)
  ;  :orientation (keyword)}
  [button-props]
  (merge {:border-color    :default
          :border-position :all
          :border-radius   {:all :m}
          :border-width    :xs
          :click-effect    :opacity
          :font-size       :s
          :max-selection   1
          :option-helper-f none
          :option-label-f  return
          :option-value-f  return
          :orientation     :vertical}
         (-> button-props)))
