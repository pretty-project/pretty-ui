
(ns pretty-elements.select.prototypes
    (:require [fruits.noop.api             :refer [return]]
              [pretty-elements.input.utils :as input.utils]
              [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-props-prototype
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:border-color (keyword or string)(opt)
  ;  :popup (map)(opt)}
  ;
  ; @return (map)
  ; {:add-option-f (function)
  ;  :border-position (keyword)
  ;  :border-width (keyword, px or string)
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
