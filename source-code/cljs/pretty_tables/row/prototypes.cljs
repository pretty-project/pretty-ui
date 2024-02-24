
(ns pretty-tables.row.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]
              [pretty-subitems.api :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn cell-props-prototype
  ; @ignore
  ;
  ; @param (integer) cell-dex
  ; @param (map) cell-props
  ;
  ; @return (map)
  [_ cell-props]
  (-> cell-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn row-props-prototype
  ; @ignore
  ;
  ; @param (keyword) row-id
  ; @param (map) row-props
  ; {:cells (maps in vector)(opt)
  ;  ...}
  ;
  ; @return (map)
  [_ {:keys [cells] :as row-props}]
  (-> row-props (pretty-properties/default-grid-props       {:row-template :even :row-count (count cells)})
                (pretty-properties/default-outer-size-props {:outer-height :content :outer-width :auto :outer-size-unit :double-block})
                (pretty-standards/standard-border-props)
                (pretty-standards/standard-inner-position-props)
                (pretty-standards/standard-inner-size-props)
                (pretty-standards/standard-outer-position-props)
                (pretty-standards/standard-outer-size-props)
                (pretty-rules/apply-auto-border-crop)
               ;(pretty-rules/auto-disable-highlight-color)
               ;(pretty-rules/auto-disable-hover-color)
                (pretty-subitems/subitem-group<-subitem-default :cells)
                (pretty-subitems/subitem-group<-disabled-state  :cells)
                (pretty-subitems/leave-disabled-state           :cells)))
