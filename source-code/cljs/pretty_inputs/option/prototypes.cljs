
(ns pretty-inputs.option.prototypes
    (:require [pretty-standards.api :as pretty-standards]
              [pretty-rules.api :as pretty-rules]
              [pretty-properties.api :as pretty-properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn props-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  [_ props]
  (-> props (pretty-properties/default-flex-props       {:horizontal-align :left :orientation :horizontal})
            (pretty-properties/default-outer-size-props {:outer-size-unit :full-block})
            (pretty-standards/standard-border-props)
            (pretty-standards/standard-flex-props)
            (pretty-standards/standard-inner-position-props)
            (pretty-standards/standard-inner-size-props)
            (pretty-standards/standard-outer-position-props)
            (pretty-standards/standard-outer-size-props)
            (pretty-rules/apply-auto-border-crop)
           ;(pretty-rules/auto-align-scrollable-flex)
            (pretty-rules/auto-blur-click-events)
            (pretty-rules/auto-disable-cursor)
            (pretty-rules/auto-disable-effects)
            (pretty-rules/auto-disable-highlight-color)
            (pretty-rules/auto-disable-hover-color)
            (pretty-rules/auto-disable-mouse-events)
            (pretty-rules/auto-set-click-effect)
            (pretty-rules/auto-set-mounted)))
