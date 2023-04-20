
(ns components.color-picker.prototypes
    (:require [components.color-picker.config :as color-picker.config]
              [components.input.utils         :as input.utils]
              [noop.api                       :refer [param]]))

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
          :placeholder  "Choose color!"
          :options      color-picker.config/DEFAULT-OPTIONS
          :options-path (input.utils/default-options-path picker-id)
          :value-path   (input.utils/default-value-path   picker-id)}
         (param picker-props)
         {:color-stamp (merge {:height :l :width :l}
                              (param color-stamp))}))
