
(ns pretty-tables.table.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api      :as pretty-rules]
              [pretty-standards.api  :as pretty-standards]
              [pretty-subitems.api   :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn row-prototype
  ; @ignore
  ;
  ; @param (integer) dex
  ; @param (map) row
  ;
  ; @return (map)
  [_ row]
  (-> row (pretty-properties/default-outer-size-props {:outer-height :content :outer-width :parent})))

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
  (-> props (pretty-properties/default-flex-props       {:orientation :vertical :vertical-align :top})
            (pretty-properties/default-outer-size-props {:outer-height :content :outer-width :auto :outer-size-unit :double-block})
            (pretty-standards/standard-border-props)
            (pretty-standards/standard-flex-props)
            (pretty-standards/standard-inner-position-props)
            (pretty-standards/standard-inner-size-props)
            (pretty-standards/standard-outer-position-props)
            (pretty-standards/standard-outer-size-props)
            (pretty-rules/apply-auto-border-crop)
            (pretty-rules/auto-align-scrollable-flex)
            (pretty-rules/auto-disable-mouse-events)
            (pretty-rules/auto-set-mounted)
            (pretty-subitems/subitem-group<-subitem-default :rows)
            (pretty-subitems/subitem-group<-disabled-state  :rows)
            (pretty-subitems/apply-group-item-prototype     :rows row-prototype)))
