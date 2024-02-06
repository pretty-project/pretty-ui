
(ns pretty-elements.chip.prototypes
    (:require [pretty-elements.properties.api :as pretty-elements.properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-props-prototype
  ; @ignore
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ;
  ; @return (map)
  [chip-id chip-props]
  (-> chip-props (pretty-elements.properties/default-background-props  {:fill-color :primary})
                 (pretty-elements.properties/default-border-props      {})
                 (pretty-elements.properties/default-effect-props      {})
                 (pretty-elements.properties/default-flex-props        {:orientation :horizontal})
                 (pretty-elements.properties/default-focus-props       {:focus-id chip-id})
                 (pretty-elements.properties/default-font-props        {:font-size :xxs :font-weight :medium})
                 (pretty-elements.properties/default-label-props       {})
                 (pretty-elements.properties/default-mouse-event-props {})
                 (pretty-elements.properties/default-space-props       {})
                 (pretty-elements.properties/default-text-props        {:text-overflow :ellipsis})))
