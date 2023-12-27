
(ns pretty-elements.counter.prototypes
    (:require [pretty-elements.input.utils :as input.utils]
              [pretty-build-kit.api :as pretty-build-kit]))

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
  ;  :border-width (keyword)
  ;  :font-size (keyword)
  ;  :initial-value (integer)
  ;  :value-path (Re-Frame path vector)}
  [counter-id counter-props]
  (merge {:border-color    :default
          :border-position :all
          :border-radius   {:all :m}
          :border-width    :xs
          :font-size       :s
          :initial-value   0
          :value-path      (input.utils/default-value-path counter-id)}
         (-> counter-props)))
