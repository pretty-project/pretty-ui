
(ns pretty-tables.data-column.prototypes
    (:require [pretty-properties.api :as pretty-properties]))

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

(defn column-props-prototype
  ; @ignore
  ;
  ; @param (keyword) column-id
  ; @param (map) column-props
  ; {:cells (maps in vector)(opt)}
  ;
  ; @return (map)
  [_ {:keys [cells] :as column-props}]
  (-> column-props (pretty-properties/default-grid-props         {:column-template :even :column-count (count cells)})
                   (pretty-properties/default-size-props         {:height :content :width :auto :size-unit :double-block})
                   (pretty-properties/default-wrapper-size-props {})))