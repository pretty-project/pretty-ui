
(ns pretty-elements.row.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn row-props-prototype
  ; @ignore
  ;
  ; @param (keyword) row-id
  ; @param (map) row-props
  ;
  ; @return (map)
  [_ row-props]
  (-> row-props (pretty-properties/default-flex-props {:orientation :horizontal}) ;:vertical-align :top})
                (pretty-properties/default-size-props {:size-unit :double-block})
                (pretty-standards/standard-body-size-props)
                (pretty-standards/standard-border-props)
                (pretty-standards/standard-flex-props)
                (pretty-standards/standard-size-props)
                (pretty-rules/apply-auto-border-crop)
                (pretty-rules/auto-align-scrollable-flex)
               ;(pretty-rules/auto-disable-highlight-color)
               ;(pretty-rules/auto-disable-hover-color)
                (pretty-rules/compose-content)))
