
(ns pretty-tables.table.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-models.api :as pretty-models]
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
            (pretty-models/flex-container-standard-props)
            (pretty-models/flex-container-rules)
            (pretty-subitems/subitem-group<-subitem-default :rows)
            (pretty-subitems/subitem-group<-disabled-state  :rows)
            (pretty-subitems/apply-group-item-prototype     :rows row-prototype)))
