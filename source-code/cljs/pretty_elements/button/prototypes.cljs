
(ns pretty-elements.button.prototypes
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
       (-> button-props (pretty-properties/default-flex-props  {:orientation :horizontal})
                        (pretty-properties/default-font-props  {:font-size :s :font-weight :medium})
                        (pretty-properties/default-react-props {:set-reference-f set-reference-f})
                        (pretty-properties/default-size-props  {:size-unit :full-block})
                        (pretty-properties/default-text-props  {:text-selectable? false})
                        (pretty-standards/standard-anchor-props)
                        (pretty-standards/standard-border-props)
                        (pretty-standards/standard-flex-props)
                        (pretty-standards/standard-font-props)
                        (pretty-standards/standard-icon-props)
                        (pretty-standards/standard-text-props)
                        (pretty-standards/standard-wrapper-size-props)
                        (pretty-rules/apply-auto-border-crop)
                        (pretty-rules/auto-adapt-wrapper-size)
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
