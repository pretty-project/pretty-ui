
(ns pretty-elements.adornment.prototypes
    (:require [pretty-elements.properties.api :as pretty-elements.properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adornment-props-prototype
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ; @param (map) adornment-props
  ;
  ; @return (map)
  [adornment-id adornment-props]
  (-> adornment-props (pretty-elements.properties/default-border-props       {})
                      (pretty-elements.properties/default-button-label-props {})
                      (pretty-elements.properties/default-content-props      {})
                      (pretty-elements.properties/default-effect-props       {})
                      (pretty-elements.properties/default-flex-props         {:orientation :horizontal})
                      (pretty-elements.properties/default-focus-props        {:focus-id adornment-id})
                      (pretty-elements.properties/default-font-props         {:font-size :xxs})
                      (pretty-elements.properties/default-icon-props         {})
                      (pretty-elements.properties/default-label-props        {})
                      (pretty-elements.properties/default-mouse-event-props  {})
                      (pretty-elements.properties/default-progress-props     {})
                      (pretty-elements.properties/default-size-props         {:min-width :xs})
                      (pretty-elements.properties/default-tooltip-props      {})))
