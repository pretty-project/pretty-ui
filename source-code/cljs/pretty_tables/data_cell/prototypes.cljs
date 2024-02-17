
(ns pretty-tables.data-cell.prototypes
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
  (-> cell-props (pretty-properties/default-font-props         {:font-size :xxs :font-weight :normal})
                 (pretty-properties/default-content-props      {})
                 (pretty-properties/default-flex-props         {:horizontal-align :left :orientation :horizontal})
                 (pretty-properties/default-size-props         {:height :parent :width :parent :size-unit :full-block})
                 (pretty-properties/default-text-props         {:text-overflow :ellipsis})
                 (pretty-standards/standard-flex-props)
                 (pretty-standards/standard-font-props)
                 (pretty-standards/standard-text-props)
                 (pretty-standards/standard-wrapper-size-props)
                 ;(pretty-rules/auto-disable-highlight-color)
                 ;(pretty-rules/auto-disable-hover-color)
                 (pretty-rules/compose-content)
                 (pretty-rules/auto-align-scrollable-flex)
                 (pretty-rules/auto-adapt-wrapper-size)))
