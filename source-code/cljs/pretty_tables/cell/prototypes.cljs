
(ns pretty-tables.cell.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn cell-props-prototype
  ; @ignore
  ;
  ; @param (keyword) cell-id
  ; @param (map) cell-props
  ;
  ; @return (map)
  [_ cell-props]
  (-> cell-props (pretty-properties/default-font-props       {:font-size :xxs :font-weight :normal})
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
                ;(pretty-rules/auto-disable-highlight-color)
                ;(pretty-rules/auto-disable-hover-color)
                 (pretty-rules/compose-content)))