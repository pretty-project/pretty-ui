
(ns pretty-tables.data-row.prototypes
    (:require [pretty-elements.properties.api :as pretty-elements.properties]))

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
  ; {:cells (maps in vector)(opt)}
  ;
  ; @return (map)
  [_ {:keys [cells] :as row-props}]
  (-> row-props (pretty-elements.properties/default-grid-props {:row-template :even :row-count (count cells)})
                (pretty-elements.properties/default-size-props {:height :content :width :auto})))
