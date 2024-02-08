
(ns pretty-elements.thumbnail.prototypes
    (:require [pretty-properties.api :as pretty-properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn thumbnail-props-prototype
  ; @ignore
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ;
  ; @return (map)
  [thumbnail-id thumbnail-props]
  (-> thumbnail-props (pretty-properties/clickable-text-auto-props)
                      (pretty-properties/default-anchor-props           {})
                      (pretty-properties/default-background-color-props {:fill-color :highlight})
                      (pretty-properties/default-background-image-props {:background-size :contain})
                      (pretty-properties/default-border-props           {})
                      (pretty-properties/default-effect-props           {})
                      (pretty-properties/default-flex-props             {:orientation :vertical})
                      (pretty-properties/default-focus-props            {:focus-id thumbnail-id})
                      (pretty-properties/default-font-props             {:font-size :xs :font-weight :medium})
                      (pretty-properties/default-icon-props             {:icon :image :icon-color :highlight})
                      (pretty-properties/default-label-props            {})
                      (pretty-properties/default-mouse-event-props      {})
                      (pretty-properties/default-size-props             {:height :s :width :s})
                      (pretty-properties/default-text-props             {:text-overflow :ellipsis :text-selectable? false})))
