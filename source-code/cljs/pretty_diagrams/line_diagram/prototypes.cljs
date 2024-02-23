
(ns pretty-diagrams.line-diagram.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-props-prototype
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;
  ; @return (map)
  [diagram-id diagram-props]
  (-> diagram-props (pretty-properties/default-flex-props       {:horizontal-align :left :orientation :horizontal})
                    (pretty-properties/default-outer-size-props {:outer-size-unit :full-block})
                    (pretty-standards/standard-data-props)
                    (pretty-standards/standard-flex-props)
                    (pretty-standards/standard-inner-position-props)
                    (pretty-standards/standard-inner-size-props)
                    (pretty-standards/standard-outer-position-props)
                    (pretty-standards/standard-outer-size-props)
                    (pretty-standards/standard-shape-props)
                    (pretty-rules/auto-align-scrollable-flex)))
