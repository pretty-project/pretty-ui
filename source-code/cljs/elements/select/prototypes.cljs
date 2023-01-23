
(ns elements.select.prototypes
    (:require [elements.input.helpers :as input.helpers]
              [noop.api               :refer [param return]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-props-prototype
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:border-color (keyword)(opt)}
  ;
  ; @return (map)
  ; {:add-option-f (function)
  ;  :border-position (keyword)
  ;  :border-width (keyword)
  ;  :option-field-placeholder (metamorphic-content)
  ;  :layout (keyword)
  ;  :option-label-f (function)
  ;  :option-value-f (function)
  ;  :options-placeholder (metamorphic-content)
  ;  :value-path (vector)}
  [select-id {:keys [border-color] :as select-props}]
  (merge {:add-option-f             return
          :option-field-placeholder :add!
          :option-label-f           return
          :option-value-f           return
          :layout                   :select
          :options-placeholder      :no-options
          :options-path             (input.helpers/default-options-path select-id)
          :value-path               (input.helpers/default-value-path   select-id)}
         (if border-color {:border-position :all
                           :border-width    :xxs})
         (param select-props)))
