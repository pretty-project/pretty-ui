
(ns pretty-elements.data-table.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn table-column-attributes
  ; @ignore
  ;
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; @param (map) column-props
  ; {}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ _ {:keys [template]}]
  {:class :pe-data-table--column
   :style {:grid-template-rows template}})

(defn table-row-attributes
  ; @ignore
  ;
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; @param (map) row-props
  ; {}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)}
  [_ _ {:keys [template]}]
  {:class :pe-data-table--row
   :style {:grid-template-columns template}})

(defn table-cell-attributes
  ; @ignore
  ;
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; @param (map) cell-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ _ cell-props]
  (-> {:class :pe-data-table--cell}
      (pretty-build-kit/color-attributes  cell-props)
      (pretty-build-kit/font-attributes   cell-props)
      (pretty-build-kit/indent-attributes cell-props)
      (pretty-build-kit/text-attributes   cell-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn table-body-attributes
  ; @ignore
  ;
  ; @param (keyword) table-id
  ; @param (map) table-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ table-props]
  (-> {:class :pe-data-table--body}
      (pretty-build-kit/indent-attributes table-props)
      (pretty-build-kit/style-attributes  table-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn table-attributes
  ; @ignore
  ;
  ; @param (keyword) table-id
  ; @param (map) table-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ table-props]
  (-> {:class :pe-data-table}
      (pretty-build-kit/class-attributes   table-props)
      (pretty-build-kit/outdent-attributes table-props)
      (pretty-build-kit/state-attributes   table-props)))
