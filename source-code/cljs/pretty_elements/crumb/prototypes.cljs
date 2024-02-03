
(ns pretty-elements.crumb.prototypes
    (:require [pretty-elements.properties.api :as pretty-elements.properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn crumb-props-prototype
  ; @ignore
  ;
  ; @param (keyword) crumb-id
  ; @param (map) crumb-props
  ;
  ; @return (map)
  [_ crumb-props]
  (-> crumb-props (pretty-elements.properties/clickable-text-auto-props)
                  (pretty-elements.properties/default-effect-props      {})
                  (pretty-elements.properties/default-flex-props        {:orientation :horizontal})
                  (pretty-elements.properties/default-font-props        {:font-size :xs :font-weight :semi-bold})
                  (pretty-elements.properties/default-label-props       {})
                  (pretty-elements.properties/default-mouse-event-props {})
                  (pretty-elements.properties/default-size-props        {:max-width :l})
                  (pretty-elements.properties/default-text-props        {:text-overflow :ellipsis})))
