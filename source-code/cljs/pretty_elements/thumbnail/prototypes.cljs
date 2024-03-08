
(ns pretty-elements.thumbnail.prototypes
    (:require [pretty-elements.image.prototypes :as image.prototypes]
              [pretty-properties.api            :as pretty-properties]
              [pretty-rules.api                 :as pretty-rules]
              [pretty-standards.api             :as pretty-standards]
              [pretty-subitems.api              :as pretty-subitems]
              [react-references.api             :as react-references]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (pretty-elements.image.prototypes/*)
(def sensor-prototype image.prototypes/sensor-prototype)
(def icon-prototype   image.prototypes/icon-prototype)
(def label-prototype  image.prototypes/label-prototype)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn props-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  [id props]
  (let [set-reference-f    (fn [%] (react-references/set-reference! id       %))
        icon-prototype-f   (fn [%] (icon-prototype                  id props %))
        label-prototype-f  (fn [%] (label-prototype                 id props %))
        sensor-prototype-f (fn [%] (label-prototype                 id props %))]
       (-> props (pretty-properties/default-animation-props        {:animation-duration 2000 :animation-mode :repeat :animation-name :pulse})
                 (pretty-properties/default-background-color-props {:fill-color :highlight})
                 (pretty-properties/default-background-image-props {:background-size :contain})
                 (pretty-properties/default-border-props           {:border-crop :auto})
                 (pretty-properties/default-content-size-props     {:content-height :grow :content-width :parent})
                 (pretty-properties/default-flex-props             {:orientation :vertical})
                 (pretty-properties/default-outer-size-props       {:outer-height :s :outer-width :s :outer-size-unit :full-block})
                 (pretty-properties/default-react-props            {:set-reference-f set-reference-f})
                 (pretty-standards/standard-anchor-props)
                 (pretty-standards/standard-animation-props)
                 (pretty-standards/standard-background-image-props)
                 (pretty-standards/standard-border-props)
                 (pretty-standards/standard-flex-props)
                 (pretty-standards/standard-inner-position-props)
                 (pretty-standards/standard-inner-size-props)
                 (pretty-standards/standard-outer-position-props)
                 (pretty-standards/standard-outer-size-props)
                 (pretty-rules/apply-auto-border-crop)
                 (pretty-rules/auto-align-scrollable-flex)
                 (pretty-rules/auto-blur-click-events)
                 (pretty-rules/auto-disable-cursor)
                 (pretty-rules/auto-disable-effects)
                 (pretty-rules/auto-disable-mouse-events)
                 (pretty-rules/auto-disable-highlight-color)
                 (pretty-rules/auto-disable-hover-color)
                 (pretty-rules/auto-set-click-effect)
                 (pretty-rules/auto-set-mounted)
                 (pretty-subitems/ensure-subitems         :icon  :sensor)
                 (pretty-subitems/apply-subitem-longhand  :label :content)
                 (pretty-subitems/apply-subitem-prototype :icon   icon-prototype-f)
                 (pretty-subitems/apply-subitem-prototype :label  label-prototype-f)
                 (pretty-subitems/apply-subitem-prototype :sensor sensor-prototype-f))))
