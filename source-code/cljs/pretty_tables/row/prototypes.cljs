
(ns pretty-tables.row.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api      :as pretty-rules]
              [pretty-standards.api  :as pretty-standards]
              [pretty-subitems.api   :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn props-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:cells (maps in vector)(opt)
  ;  ...}
  ;
  ; @return (map)
  [_ {:keys [cells] :as props}]
  (-> props (pretty-properties/default-grid-props       {:row-template :even :row-count (count cells)})
            (pretty-properties/default-outer-size-props {:outer-height :content :outer-width :auto :outer-size-unit :double-block})
            (pretty-standards/standard-border-props)
            (pretty-standards/standard-inner-position-props)
            (pretty-standards/standard-inner-size-props)
            (pretty-standards/standard-outer-position-props)
            (pretty-standards/standard-outer-size-props)
            (pretty-rules/apply-auto-border-crop)
            (pretty-rules/auto-set-mounted)
            (pretty-subitems/subitem-group<-subitem-default :cells)
            (pretty-subitems/subitem-group<-disabled-state  :cells)
            (pretty-subitems/leave-disabled-state           :cells)))
