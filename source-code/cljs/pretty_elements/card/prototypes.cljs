
(ns pretty-elements.card.prototypes
    (:require [pretty-elements.properties.api :as pretty-elements.properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn card-props-prototype
  ; @ignore
  ;
  ; @param (keyword) card-id
  ; @param (map) card-props
  ;
  ; @return (map)
  [card-id card-props]
  (-> card-props (pretty-elements.properties/default-badge-props       {})
                 (pretty-elements.properties/default-border-props      {})
                 (pretty-elements.properties/default-mouse-event-props {})
                 (pretty-elements.properties/default-content-props     {})
                 (pretty-elements.properties/default-effect-props      {})
                 (pretty-elements.properties/default-flex-props        {:orientation :vertical :vertical-align :top})
                 (pretty-elements.properties/default-focus-props       {:focus-id card-id})
                 (pretty-elements.properties/default-marker-props      {})
                 (pretty-elements.properties/default-tooltip-props     {})))
