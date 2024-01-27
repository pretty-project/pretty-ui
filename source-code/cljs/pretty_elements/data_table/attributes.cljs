
(ns pretty-elements.data-table.attributes
    (:require [pretty-css.api :as pretty-css]
              [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.layout.api :as pretty-css.layout]))

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
      (pretty-css.appearance/background-attributes           cell-props)
      (pretty-css/font-attributes            cell-props)
      (pretty-css.layout/indent-attributes          cell-props)
      (pretty-css/selectable-text-attributes cell-props)))

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
      (pretty-css.layout/indent-attributes table-props)
      (pretty-css/style-attributes  table-props)))

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
      (pretty-css/class-attributes   table-props)
      (pretty-css.layout/outdent-attributes table-props)
      (pretty-css/state-attributes   table-props)
      (pretty-css/theme-attributes   table-props)))
