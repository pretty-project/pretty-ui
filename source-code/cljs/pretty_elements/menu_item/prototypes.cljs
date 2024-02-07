
(ns pretty-elements.menu-item.prototypes
    (:require [pretty-elements.menu-item.side-effects :as menu-item.side-effects]
              [pretty-properties.api                  :as pretty-properties]))

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
       (-> item-props (pretty-properties/clickable-text-auto-props)
                      (pretty-properties/inherit-icon-props)
                      (pretty-properties/default-anchor-props      {})
                      (pretty-properties/default-background-props  {})
                      (pretty-properties/default-badge-props       {})
                      (pretty-properties/default-border-props      {})
                      (pretty-properties/default-effect-props      {})
                      (pretty-properties/default-focus-props       {:focus-id item-id})
                      (pretty-properties/default-font-props        {:font-size :s :font-weight :medium})
                      (pretty-properties/default-icon-props        {})
                      (pretty-properties/default-label-props       {})
                      (pretty-properties/default-marker-props      {})
                      (pretty-properties/default-mouse-event-props {:on-mouse-over-f on-mouse-over-f})
                      (pretty-properties/default-flex-props        {:orientation :horizontal})
                      (pretty-properties/default-text-props        {:text-selectable? false}))))
