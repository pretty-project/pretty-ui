
(ns pretty-tables.row.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-models.api      :as pretty-models]
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
            (pretty-models/container-model-standard-props)
            (pretty-models/container-model-rules)
            (pretty-subitems/subitem-group<-subitem-default :cells)
            (pretty-subitems/subitem-group<-disabled-state  :cells)))
