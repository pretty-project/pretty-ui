
(ns pretty-inputs.switch.prototypes
    (:require [fruits.noop.api           :refer [none return]]
              [pretty-build-kit.api      :as pretty-build-kit]
              [pretty-inputs.input.utils :as input.utils]))

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
  ;  :border-width (keyword, px or string)
  ;  :click-effect (keyword)
  ;  :font-size (keyword, px or string)
  ;  :option-helper-f (function)
  ;  :option-label-f (function)
  ;  :option-value-f (function)
  ;  :orientation (keyword)
  ;  :options-path (Re-Frame path vector)
  ;  :value-path (Re-Frame path vector)}
  [switch-id switch-props]
  (merge {:border-color    :default
          :border-position :all
          :border-radius   {:all :m}
          :border-width    :xs
          :click-effect    :opacity
          :font-size       :s
          :option-helper-f none
          :option-label-f  return
          :option-value-f  return
          :orientation     :vertical
          :options-path    (input.utils/default-options-path switch-id)
          :value-path      (input.utils/default-value-path   switch-id)}
         (-> switch-props)))
