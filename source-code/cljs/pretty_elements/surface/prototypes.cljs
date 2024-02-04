
(ns pretty-elements.surface.prototypes
    (:require [pretty-elements.properties.api :as pretty-elements.properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-props-prototype
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ;
  ; @return (map)
  [_ surface-props]
  (-> surface-props (pretty-elements.properties/default-border-props  {})
                    (pretty-elements.properties/default-content-props {})
                    (pretty-elements.properties/default-flex-props    {:horizontal-align :left :orientation :vertical :vertical-align :top})
                    (pretty-elements.properties/default-size-props    {:height :content :width :auto})))
