
(ns pretty-elements.thumbnail.prototypes
    (:require [pretty-elements.image.prototypes :as image.prototypes]
              [react-references.api :as react-references]
              [pretty-properties.api :as pretty-properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (pretty-elements.image.prototypes/*)
(def sensor-props-prototype image.prototypes/sensor-props-prototype)

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
  (let [set-reference-f (react-references/set-reference-f thumbnail-id)]
       (-> thumbnail-props (pretty-properties/clickable-text-auto-props)
                           (pretty-properties/default-anchor-props           {})
                           (pretty-properties/default-animation-props        {:animation-duration 2000 :animation-mode :repeat :animation-name :pulse})
                           (pretty-properties/default-background-color-props {:fill-color :highlight})
                           (pretty-properties/default-background-image-props {:background-size :contain})
                           (pretty-properties/default-border-props           {})
                           (pretty-properties/default-canvas-size-props      {:canvas-height :grow :canvas-width :parent})
                           (pretty-properties/default-effect-props           {})
                           (pretty-properties/default-flex-props             {:orientation :vertical})
                           (pretty-properties/default-font-props             {:font-size :xs :font-weight :medium})
                           (pretty-properties/default-icon-props             {:icon :image :icon-color :muted})
                           (pretty-properties/default-label-props            {})
                           (pretty-properties/default-mouse-event-props      {})
                           (pretty-properties/default-react-props            {:set-reference-f set-reference-f})
                           (pretty-properties/default-size-props             {:height :s :width :s :size-unit :full-block})
                           (pretty-properties/default-text-props             {:text-overflow :ellipsis :text-selectable? false})
                           (pretty-properties/default-wrapper-size-props     {}))))
