
(ns pretty-elements.button.prototypes
    (:require [pretty-properties.api :as pretty-properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  [button-id button-props]
  (-> button-props (pretty-properties/clickable-text-auto-props)
                   (pretty-properties/inherit-icon-props)
                   (pretty-properties/default-anchor-props      {})
                   (pretty-properties/default-border-props      {})
                   (pretty-properties/default-effect-props      {})
                   (pretty-properties/default-flex-props        {:orientation :horizontal})
                   (pretty-properties/default-focus-props       {:focus-id button-id})
                   (pretty-properties/default-font-props        {:font-size :s :font-weight :medium})
                   (pretty-properties/default-icon-props        {})
                   (pretty-properties/default-label-props       {})
                   (pretty-properties/default-mouse-event-props {})
                   (pretty-properties/default-progress-props    {})
                   (pretty-properties/default-text-props        {:text-selectable? false})))
