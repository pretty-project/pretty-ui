
(ns pretty-inputs.counter.prototypes
    (:require [pretty-standards.api :as pretty-standards]
              [pretty-rules.api :as pretty-rules]
              [pretty-properties.api :as pretty-properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn counter-props-prototype
  ; @ignore
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;
  ; @return (map)
  ; {:border-color (keyword or string)
  ;  :border-radius (map)
  ;  :border-position (keyword)
  ;  :border-width (keyword, px or string)
  ;  :font-size (keyword, px or string)
  ;  :initial-value (integer)
  ;  :value-path (Re-Frame path vector)}
  [counter-id counter-props]
  (merge {:border-color    :default
          :border-position :all
          :border-radius   {:all :m}
          :border-width    :xs
          :font-size       :s
          :initial-value   0}
         (-> counter-props)))
; standard-input-option-props
