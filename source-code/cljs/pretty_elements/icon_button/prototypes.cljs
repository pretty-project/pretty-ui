
(ns pretty-elements.icon-button.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [react-references.api :as react-references]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]))

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
  (let [set-reference-f (react-references/set-reference-f button-id)]
       (-> button-props (pretty-properties/default-flex-props       {:orientation :vertical})
                        (pretty-properties/default-font-props       {:font-size :micro :font-weight :medium})
                        (pretty-properties/default-icon-props       {:icon-size :m})
                        (pretty-properties/default-outer-size-props {:outer-size-unit :full-block})
                        (pretty-properties/default-react-props      {:set-reference-f set-reference-f})
                        (pretty-properties/default-text-props       {:text-selectable? false})
                        (pretty-standards/standard-anchor-props)
                        (pretty-standards/standard-border-props)
                        (pretty-standards/standard-flex-props)
                        (pretty-standards/standard-font-props)
                        (pretty-standards/standard-icon-props)
                        (pretty-standards/standard-inner-size-props)
                        (pretty-standards/standard-outer-size-props)
                        (pretty-standards/standard-text-props)
                        (pretty-rules/apply-auto-border-crop)
                        (pretty-rules/auto-align-scrollable-flex)
                        (pretty-rules/auto-blur-click-events)
                        (pretty-rules/auto-color-clickable-text)
                        (pretty-rules/auto-disable-cursor)
                        (pretty-rules/auto-disable-effects)
                        (pretty-rules/auto-disable-highlight-color)
                        (pretty-rules/auto-disable-hover-color)
                        (pretty-rules/auto-disable-mouse-events)
                        (pretty-rules/auto-set-click-effect)
                        (pretty-rules/compose-label)
                        (pretty-rules/inherit-icon-props))))
