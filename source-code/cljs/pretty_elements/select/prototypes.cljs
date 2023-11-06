
(ns pretty-elements.select.prototypes
    (:require [pretty-elements.input.utils :as input.utils]
              [noop.api             :refer [return]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-props-prototype
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:border-color (keyword)(opt)
  ;  :popup (map)(opt)}
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
  ;  :popup (map)
  ;   {:cover-color (keyword or string)
  ;    :fill-color (keyword or string)}
  ;  :value-path (Re-Frame path vector)}
  [select-id {:keys [border-color popup] :as select-props}]
  (merge {:add-option-f             return
          :option-field-placeholder :add!
          :option-label-f           return
          :option-value-f           return
          :layout                   :select-button
          :options-placeholder      :no-options
          :options-path             (input.utils/default-options-path select-id)
          :value-path               (input.utils/default-value-path   select-id)}
         (if border-color {:border-position :all
                           :border-width    :xxs})
         (-> select-props)
         {:popup (merge {:cover-color :black :fill-color :default} popup)}))