
(ns elements.data-table.attributes
    (:require [pretty-css.api :as pretty-css]))

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
  ; {:class (keyword or keywords in vector)
  ;  :style (map)}
  [_ _ {:keys [template]}]
  {:class :e-data-table--column
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
  {:class :e-data-table--row
   :style {:grid-template-columns template}})

(defn table-cell-attributes
  ; @ignore
  ;
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; @param (map) cell-props
  ; {}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-horizontal-text-align (keyword)
  ;  :data-selectable (boolean)}
  [_ _ {:keys [horizontal-align selectable?] :as cell-props}]
  (-> {:class                      :e-data-table--cell
       :data-horizontal-text-align horizontal-align
       :data-selectable            selectable?}
      (pretty-css/color-attributes  cell-props)
      (pretty-css/font-attributes   cell-props)
      (pretty-css/indent-attributes cell-props)
      (pretty-css/text-attributes   cell-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn table-body-attributes
  ; @ignore
  ;
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)}
  [_ {:keys [style] :as table-props}]
  (-> {:class :e-data-table--body
       :style style}
      (pretty-css/indent-attributes table-props)))

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
  (-> {:class :e-data-table}
      (pretty-css/default-attributes table-props)
      (pretty-css/outdent-attributes table-props)))
