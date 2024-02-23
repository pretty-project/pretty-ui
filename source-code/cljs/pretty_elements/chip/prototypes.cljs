
(ns pretty-elements.chip.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [react-references.api :as react-references]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-props-prototype
  ; @ignore
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ;
  ; @return (map)
  [chip-id chip-props]
  (let [set-reference-f (react-references/set-reference-f chip-id)]
       (-> chip-props (pretty-properties/default-background-color-props {:fill-color :primary})
                      (pretty-properties/default-flex-props             {:orientation :horizontal})
                      (pretty-properties/default-font-props             {:font-size :xxs :font-weight :medium})
                      (pretty-properties/default-outer-size-props       {:outer-size-unit :full-block})
                      (pretty-properties/default-react-props            {:set-reference-f set-reference-f})
                      (pretty-properties/default-text-props             {:text-overflow :ellipsis :text-selectable? false})
                      (pretty-standards/standard-anchor-props)
                      (pretty-standards/standard-border-props)
                      (pretty-standards/standard-flex-props)
                      (pretty-standards/standard-font-props)
                      (pretty-standards/standard-inner-position-props)
                      (pretty-standards/standard-inner-size-props)
                      (pretty-standards/standard-outer-position-props)
                      (pretty-standards/standard-outer-size-props)
                      (pretty-standards/standard-text-props)
                      (pretty-rules/apply-auto-border-crop)
                      (pretty-rules/auto-align-scrollable-flex)
                      (pretty-rules/auto-blur-click-events)
                     ;(pretty-rules/auto-color-clickable-text)
                      (pretty-rules/auto-disable-cursor)
                      (pretty-rules/auto-disable-effects)
                      (pretty-rules/auto-disable-highlight-color)
                      (pretty-rules/auto-disable-hover-color)
                      (pretty-rules/auto-disable-mouse-events)
                      (pretty-rules/auto-set-click-effect)
                      (pretty-rules/compose-label))))
