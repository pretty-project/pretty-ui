
(ns pretty-tables.table.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn row-props-prototype
  ; @ignore
  ;
  ; @param (integer) row-dex
  ; @param (map) row-props
  ;
  ; @return (map)
  [_ row-props]
  (-> row-props (pretty-properties/default-outer-size-props {:outer-height :content :outer-width :parent})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn table-props-prototype
  ; @ignore
  ;
  ; @param (keyword) table-id
  ; @param (map) table-props
  ;
  ; @return (map)
  [_ table-props]
  (-> table-props (pretty-properties/default-flex-props       {:orientation :vertical :vertical-align :top})
                  (pretty-properties/default-outer-size-props {:outer-height :content :outer-width :auto :outer-size-unit :double-block})
                  (pretty-standards/standard-border-props)
                  (pretty-standards/standard-flex-props)
                  (pretty-standards/standard-inner-position-props)
                  (pretty-standards/standard-inner-size-props)
                  (pretty-standards/standard-outer-position-props)
                  (pretty-standards/standard-outer-size-props)
                  (pretty-rules/apply-auto-border-crop)
                  (pretty-rules/auto-align-scrollable-flex)))
                 ;(pretty-rules/auto-disable-highlight-color)
                 ;(pretty-rules/auto-disable-hover-color)
