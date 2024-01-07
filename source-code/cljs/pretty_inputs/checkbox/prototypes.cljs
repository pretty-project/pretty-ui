
(ns pretty-inputs.checkbox.prototypes
    (:require [fruits.noop.api             :refer [return]]
              [pretty-inputs.input.utils :as input.utils]
              [pretty-build-kit.api :as pretty-build-kit]
              [pretty-inputs.checkbox.side-effects :as checkbox.side-effects]))

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
  ;  :border-width (keyword, px or string)
  ;  :click-effect (keyword)
  ;  :font-size (keyword, px or string)
  ;  :option-label-f (function)
  ;  :option-value-f (function)
  ;  :options-orientation (keyword)
  ;  :options-path (Re-Frame path vector)
  ;  :value-path (Re-Frame path vector)}
  [checkbox-id checkbox-props]
  (merge {:border-color        :default
          :border-position     :all
          :border-width        :xs
          :click-effect        :opacity
          :font-size           :s
          :option-label-f      return
          :option-value-f      return
          :options-orientation :vertical
          :on-change           (checkbox.side-effects/default-on-change-f checkbox-id checkbox-props)
          :options-path        (input.utils/default-options-path          checkbox-id)
          :value-path          (input.utils/default-value-path            checkbox-id)}
         (-> checkbox-props)))
