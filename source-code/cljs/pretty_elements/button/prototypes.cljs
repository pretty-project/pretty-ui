
(ns pretty-elements.button.prototypes
    (:require [pretty-elements.properties.api :as pretty-elements.properties]))

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
  (-> button-props (pretty-elements.properties/default-badge-props        {})
                   (pretty-elements.properties/default-border-props       {})
                   (pretty-elements.properties/default-button-label-props {})
                   (pretty-elements.properties/default-effect-props       {})
                   (pretty-elements.properties/default-focus-props        {:focus-id button-id})
                   (pretty-elements.properties/default-font-props         {:font-size :s :font-weight :medium})
                   (pretty-elements.properties/default-label-icon-props   {})
                   (pretty-elements.properties/default-label-props        {})
                   (pretty-elements.properties/default-marker-props       {})
                   (pretty-elements.properties/default-mouse-event-props  {})
                   (pretty-elements.properties/default-progress-props     {})
                   (pretty-elements.properties/default-flex-props         {:orientation :horizontal})
                   (pretty-elements.properties/default-text-props         {})
                   (pretty-elements.properties/default-tooltip-props      {})))
