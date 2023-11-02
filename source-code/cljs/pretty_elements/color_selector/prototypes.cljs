
(ns pretty-elements.color-selector.prototypes
    (:require [pretty-elements.color-selector.config :as color-selector.config]
              [pretty-elements.input.utils           :as input.utils]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn selector-props-prototype
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; {:popup (map)(opt)}
  ;
  ; @return (map)
  ; {:options (strings in vector)
  ;  :options-path (Re-Frame path vector)
  ;  :popup (map)
  ;   {:cover-color (keyword or string)
  ;    :fill-color (keyword or string)}
  ;  :value-path (Re-Frame path vector)}
  [selector-id {:keys [popup] :as selector-props}]
  (merge {:options color-selector.config/DEFAULT-OPTIONS
          :options-path (input.utils/default-options-path selector-id)
          :value-path   (input.utils/default-value-path   selector-id)}
         (-> selector-props)
         {:popup (merge {:cover-color :black :fill-color :default} popup)}))
