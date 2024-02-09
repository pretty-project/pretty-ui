
(ns pretty-tables.data-table.prototypes
    (:require [pretty-properties.api :as pretty-properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn column-props-prototype
  ; @ignore
  ;
  ; @param (integer) column-dex
  ; @param (map) column-props
  ;
  ; @return (map)
  [_ column-props]
  (-> column-props))

(defn row-props-prototype
  ; @ignore
  ;
  ; @param (integer) row-dex
  ; @param (map) row-props
  ;
  ; @return (map)
  [_ row-props]
  (-> row-props))

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
  (-> table-props (pretty-properties/default-size-props         {:height :content :width :auto :size-unit :double-block})
                  (pretty-properties/default-wrapper-size-props {})))
