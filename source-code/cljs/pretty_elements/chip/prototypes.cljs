
(ns pretty-elements.chip.prototypes
    (:require [pretty-properties.api :as pretty-properties]))

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
  (-> chip-props (pretty-properties/clickable-text-auto-props)
                 (pretty-properties/default-anchor-props      {})
                 (pretty-properties/default-background-props  {:fill-color :primary})
                 (pretty-properties/default-border-props      {})
                 (pretty-properties/default-effect-props      {})
                 (pretty-properties/default-flex-props        {:orientation :horizontal})
                 (pretty-properties/default-focus-props       {:focus-id chip-id})
                 (pretty-properties/default-font-props        {:font-size :xxs :font-weight :medium})
                 (pretty-properties/default-label-props       {})
                 (pretty-properties/default-mouse-event-props {})
                 (pretty-properties/default-space-props       {})
                 (pretty-properties/default-size-props        {})
                 (pretty-properties/default-text-props        {:text-overflow :ellipsis :text-selectable? false})))
