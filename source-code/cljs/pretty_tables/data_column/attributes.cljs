
(ns pretty-tables.data-column.attributes
    (:require [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.basic.api      :as pretty-css.basic]
              [pretty-css.layout.api     :as pretty-css.layout]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn column-body-attributes
  ; @ignore
  ;
  ; @param (keyword) column-id
  ; @param (map) column-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ column-props]
  (-> {:class :pt-data-column--body}
      (pretty-css.layout/grid-attributes              column-props)
      (pretty-css.layout/double-block-size-attributes column-props)
      (pretty-css.layout/indent-attributes            column-props)
      (pretty-css.basic/style-attributes              column-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn column-attributes
  ; @ignore
  ;
  ; @param (keyword) column-id
  ; @param (map) column-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ column-props]
  (-> {:class :pt-data-column}
      (pretty-css.appearance/theme-attributes    column-props)
      (pretty-css.basic/class-attributes         column-props)
      (pretty-css.basic/state-attributes         column-props)
      (pretty-css.layout/outdent-attributes      column-props)
      (pretty-css.layout/wrapper-size-attributes column-props)))
