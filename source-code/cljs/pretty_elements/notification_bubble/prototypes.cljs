
(ns pretty-elements.notification-bubble.prototypes
    (:require [react-references.api :as react-references]
              [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bubble-props-prototype
  ; @ignore
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ;
  ; @return (map)
  [bubble-id bubble-props]
  (let [set-reference-f (react-references/set-reference-f bubble-id)]
       (-> bubble-props (pretty-properties/default-flex-props  {:orientation :horizontal :horizontal-align :left :gap :auto})
                        (pretty-properties/default-font-props  {:font-size :s :font-weight :medium})
                        (pretty-properties/default-react-props {:set-reference-f set-reference-f})
                        (pretty-properties/default-size-props  {:size-unit :double-block})
                        (pretty-properties/default-text-props  {:text-overflow :wrap :text-selectable? true})
                        (pretty-standards/standard-anchor-props)
                        (pretty-standards/standard-body-size-props)
                        (pretty-standards/standard-border-props)
                        (pretty-standards/standard-flex-props)
                        (pretty-standards/standard-font-props)
                        (pretty-standards/standard-icon-props)
                        (pretty-standards/standard-size-props)
                        (pretty-standards/standard-text-props)
                        (pretty-rules/apply-auto-border-crop)
                        (pretty-rules/auto-align-scrollable-flex)
                        (pretty-rules/auto-blur-click-events)
                        (pretty-rules/auto-disable-cursor)
                        (pretty-rules/auto-disable-effects)
                        (pretty-rules/auto-disable-highlight-color)
                        (pretty-rules/auto-disable-hover-color)
                        (pretty-rules/auto-disable-mouse-events)
                        (pretty-rules/auto-set-click-effect)
                        (pretty-rules/compose-content)
                        (pretty-rules/inherit-icon-props))))
