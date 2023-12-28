
(ns pretty-elements.switch.prototypes
    (:require [fruits.noop.api             :refer [return]]
              [pretty-elements.input.utils :as input.utils]
              [pretty-build-kit.api :as pretty-build-kit]))

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
  ;  :option-label-f (function)
  ;  :option-value-f (function)
  ;  :options-orientation (keyword)
  ;  :options-path (Re-Frame path vector)
  ;  :value-path (Re-Frame path vector)}
  [switch-id switch-props]
  (merge {:border-color        :default
          :border-position     :all
          :border-radius       {:all :m}
          :border-width        :xs
          :click-effect        :opacity
          :font-size           :s
          :option-label-f      return
          :option-value-f      return
          :options-orientation :vertical
          :options-path        (input.utils/default-options-path switch-id)
          :value-path          (input.utils/default-value-path   switch-id)}
         (-> switch-props)))
