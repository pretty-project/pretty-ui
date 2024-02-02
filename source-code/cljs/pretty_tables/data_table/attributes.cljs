
(ns pretty-tables.data-table.attributes
    (:require [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.basic.api      :as pretty-css.basic]
              [pretty-css.content.api    :as pretty-css.content]
              [pretty-css.layout.api     :as pretty-css.layout]))

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
      (pretty-css.basic/style-attributes              table-props)
      (pretty-css.layout/double-block-size-attributes table-props)
      (pretty-css.layout/indent-attributes            table-props)))

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
      (pretty-css.appearance/theme-attributes    table-props)
      (pretty-css.basic/class-attributes         table-props)
      (pretty-css.basic/state-attributes         table-props)
      (pretty-css.layout/outdent-attributes      table-props)
      (pretty-css.layout/wrapper-size-attributes table-props)))
