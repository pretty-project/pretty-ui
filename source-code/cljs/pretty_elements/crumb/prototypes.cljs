
(ns pretty-elements.crumb.prototypes
    (:require [pretty-properties.api :as pretty-properties]))

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
  (-> crumb-props (pretty-properties/clickable-text-auto-props)
                  (pretty-properties/default-anchor-props      {})
                  (pretty-properties/default-effect-props      {})
                  (pretty-properties/default-flex-props        {:orientation :horizontal})
                  (pretty-properties/default-font-props        {:font-size :xs :font-weight :semi-bold})
                  (pretty-properties/default-label-props       {})
                  (pretty-properties/default-mouse-event-props {})
                  (pretty-properties/default-size-props        {:max-width :l})
                  (pretty-properties/default-text-props        {:text-overflow :ellipsis :text-selectable? false})))
