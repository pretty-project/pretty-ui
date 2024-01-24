
(ns pretty-inputs.checkbox.prototypes
    (:require [fruits.noop.api      :refer [none return]]
              [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn checkbox-props-prototype
  ; @ignore
  ;
  ; @param (map) checkbox-props
  ;
  ; @return (map)
  ; {:border-color (keyword or string)
  ;  :border-position (keyword)
  ;  :border-width (keyword, px or string)
  ;  :click-effect (keyword)
  ;  :font-size (keyword, px or string)
  ;  :option-helper-f (function)
  ;  :option-label-f (function)
  ;  :option-value-f (function)
  ;  :orientation (keyword)}
  [checkbox-id checkbox-props]
  (merge {:border-color    :default
          :border-position :all
          :border-width    :xs
          :click-effect    :opacity
          :font-size       :s
          :option-helper-f none
          :option-label-f  return
          :option-value-f  return
          :orientation     :vertical}
         (-> checkbox-props)))
