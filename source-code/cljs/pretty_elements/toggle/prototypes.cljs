
(ns pretty-elements.toggle.prototypes
    (:require [react-references.api :as react-references]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]
              [pretty-properties.api :as pretty-properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-props-prototype
  ; @ignore
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  ;
  ; @return (map)
  [toggle-id toggle-props]
  (let [set-reference-f (react-references/set-reference-f toggle-id)]
       (-> toggle-props (pretty-properties/default-flex-props  {:orientation :horizontal})
                        (pretty-properties/default-react-props {:set-reference-f set-reference-f})
                        (pretty-properties/default-size-props  {:size-unit :full-block})
                        (pretty-standards/standard-anchor-props)
                        (pretty-standards/standard-body-size-props)
                        (pretty-standards/standard-border-props)
                        (pretty-standards/standard-flex-props)
                        (pretty-standards/standard-size-props)
                        (pretty-rules/apply-auto-border-crop)
                        (pretty-rules/auto-align-scrollable-flex)
                        (pretty-rules/auto-blur-click-events)
                        (pretty-rules/auto-disable-cursor)
                        (pretty-rules/auto-disable-effects)
                        (pretty-rules/auto-disable-highlight-color)
                        (pretty-rules/auto-disable-hover-color)
                        (pretty-rules/auto-disable-mouse-events)
                        (pretty-rules/auto-set-click-effect)
                        (pretty-rules/compose-content))))
