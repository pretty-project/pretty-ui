
(ns components.color-picker.prototypes
    (:require [components.input.utils         :as input.utils]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn picker-props-prototype
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [picker-id {:keys [color-stamp] :as picker-props}]
  (merge {:click-effect :opacity
          :placeholder  :choose-color!
          :options-path (input.utils/default-options-path picker-id)
          :value-path   (input.utils/default-value-path   picker-id)}
         (-> picker-props)
         {:color-stamp (merge {:height :l :width :l}
                              (-> color-stamp))}))
