
(ns pretty-elements.column.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn column-props-prototype
  ; @ignore
  ;
  ; @param (keyword) column-id
  ; @param (map) column-props
  ;
  ; @return (map)
  [_ column-props]
  (-> column-props (pretty-properties/default-flex-props       {:orientation :vertical :vertical-align :top})
                   (pretty-properties/default-outer-size-props {:outer-size-unit :double-block})
                   (pretty-standards/standard-border-props)
                   (pretty-standards/standard-flex-props)
                   (pretty-standards/standard-inner-position-props)
                   (pretty-standards/standard-inner-size-props)
                   (pretty-standards/standard-outer-position-props)
                   (pretty-standards/standard-outer-size-props)
                   (pretty-rules/apply-auto-border-crop)
                   (pretty-rules/auto-align-scrollable-flex)
                   (pretty-rules/auto-disable-highlight-color)
                   (pretty-rules/auto-disable-hover-color)
                   (pretty-rules/compose-content)))
