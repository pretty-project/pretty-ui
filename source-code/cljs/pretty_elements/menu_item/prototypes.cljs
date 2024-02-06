
(ns pretty-elements.menu-item.prototypes
    (:require [pretty-elements.properties.api :as pretty-elements.properties]
              [pretty-elements.menu-item.side-effects :as menu-item.side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-props-prototype
  ; @ignore
  ;
  ; @param (keyword) item-id
  ; @param (map) item-props
  ;
  ; @return (map)
  [item-id item-props]
  (let [on-mouse-over-f (fn [_] (menu-item.side-effects/on-mouse-over-f item-id item-props))]
       (-> item-props (pretty-elements.properties/inherit-icon-props)
                      (pretty-elements.properties/clickable-text-auto-props)
                      (pretty-elements.properties/default-background-props  {})
                      (pretty-elements.properties/default-badge-props       {})
                      (pretty-elements.properties/default-border-props      {})
                      (pretty-elements.properties/default-effect-props      {})
                      (pretty-elements.properties/default-focus-props       {:focus-id item-id})
                      (pretty-elements.properties/default-font-props        {:font-size :s :font-weight :medium})
                      (pretty-elements.properties/default-icon-props        {})
                      (pretty-elements.properties/default-label-props       {})
                      (pretty-elements.properties/default-marker-props      {})
                      (pretty-elements.properties/default-mouse-event-props {:on-mouse-over-f on-mouse-over-f})
                      (pretty-elements.properties/default-flex-props        {:orientation :horizontal})
                      (pretty-elements.properties/default-text-props        {}))))
