
(ns pretty-tables.cell.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]))

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
  (-> props (pretty-properties/default-font-props       {:font-size :xxs :font-weight :normal})
            (pretty-properties/default-flex-props       {:horizontal-align :center :orientation :horizontal})
            (pretty-properties/default-outer-size-props {:outer-height :content :outer-width :auto :outer-size-unit :full-block})
            (pretty-properties/default-text-props       {:text-overflow :ellipsis})
            (pretty-standards/standard-border-props)
            (pretty-standards/standard-flex-props)
            (pretty-standards/standard-font-props)
            (pretty-standards/standard-inner-position-props)
            (pretty-standards/standard-inner-size-props)
            (pretty-standards/standard-outer-position-props)
            (pretty-standards/standard-outer-size-props)
            (pretty-standards/standard-text-props)
            (pretty-rules/auto-align-scrollable-flex)
            (pretty-rules/auto-set-mounted)
            (pretty-rules/compose-content)))
