
(ns pretty-tables.data-cell.attributes
    (:require [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.basic.api      :as pretty-css.basic]
              [pretty-css.content.api    :as pretty-css.content]
              [pretty-css.layout.api     :as pretty-css.layout]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn cell-body-attributes
  ; @ignore
  ;
  ; @param (keyword) cell-id
  ; @param (map) cell-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ cell-props]
  (-> {:class :pt-data-cell--body}
      (pretty-css.appearance/background-attributes   cell-props)
      (pretty-css.basic/style-attributes             cell-props)
      (pretty-css.content/font-attributes            cell-props)
      (pretty-css.content/selectable-text-attributes cell-props)
      (pretty-css.layout/flex-attributes             cell-props)
      (pretty-css.layout/full-block-size-attributes  cell-props)
      (pretty-css.layout/indent-attributes           cell-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn cell-attributes
  ; @ignore
  ;
  ; @param (keyword) cell-id
  ; @param (map) cell-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ cell-props]
  (-> {:class :pt-data-cell}
      (pretty-css.appearance/theme-attributes    cell-props)
      (pretty-css.basic/class-attributes         cell-props)
      (pretty-css.basic/state-attributes         cell-props)
      (pretty-css.layout/outdent-attributes      cell-props)
      (pretty-css.layout/wrapper-size-attributes cell-props)))
