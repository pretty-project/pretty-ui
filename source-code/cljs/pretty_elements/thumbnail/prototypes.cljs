
(ns pretty-elements.thumbnail.prototypes
    (:require [pretty-elements.image.prototypes :as image.prototypes]
              [react-references.api :as react-references]
              [pretty-properties.api :as pretty-properties]
              [pretty-standards.api :as pretty-standards]
              [pretty-rules.api :as pretty-rules]))

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
       (-> thumbnail-props (pretty-properties/default-animation-props        {:animation-duration 2000 :animation-mode :repeat :animation-name :pulse})
                           (pretty-properties/default-background-color-props {:fill-color :highlight})
                           (pretty-properties/default-background-image-props {:background-size :contain})
                           (pretty-properties/default-border-props           {:border-crop :auto})
                           (pretty-properties/default-content-size-props     {:content-height :grow :content-width :parent})
                           (pretty-properties/default-flex-props             {:orientation :vertical})
                           (pretty-properties/default-font-props             {:font-size :xs :font-weight :medium})
                           (pretty-properties/default-icon-props             {:icon :image :icon-color :muted})
                           (pretty-properties/default-outer-size-props       {:outer-height :s :outer-width :s :outer-size-unit :full-block})
                           (pretty-properties/default-react-props            {:set-reference-f set-reference-f})
                           (pretty-properties/default-text-props             {:text-overflow :ellipsis :text-selectable? false})
                           (pretty-standards/standard-anchor-props)
                           (pretty-standards/standard-animation-props)
                           (pretty-standards/standard-background-image-props)
                           (pretty-standards/standard-border-props)
                           (pretty-standards/standard-flex-props)
                           (pretty-standards/standard-font-props)
                           (pretty-standards/standard-icon-props)
                           (pretty-standards/standard-inner-position-props)
                           (pretty-standards/standard-inner-size-props)
                           (pretty-standards/standard-outer-position-props)
                           (pretty-standards/standard-outer-size-props)
                           (pretty-standards/standard-text-props)
                           (pretty-rules/apply-auto-border-crop)
                           (pretty-rules/auto-align-scrollable-flex)
                           (pretty-rules/auto-blur-click-events)
                           (pretty-rules/auto-disable-cursor)
                           (pretty-rules/auto-disable-effects)
                           (pretty-rules/auto-disable-mouse-events)
                           (pretty-rules/auto-disable-highlight-color)
                           (pretty-rules/auto-disable-hover-color)
                           (pretty-rules/auto-set-click-effect)
                           (pretty-rules/compose-label))))
