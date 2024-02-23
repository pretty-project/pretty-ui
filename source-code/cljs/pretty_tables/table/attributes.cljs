
(ns pretty-tables.table.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn table-body-attributes
  ; @ignore
  ;
  ; @param (keyword) table-id
  ; @param (map) table-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ table-props]
  (-> {:class :pe-table--body}
      (pretty-attributes/background-color-attributes table-props)
      (pretty-attributes/border-attributes           table-props)
      (pretty-attributes/flex-attributes             table-props)
      (pretty-attributes/indent-attributes           table-props)
      (pretty-attributes/inner-size-attributes       table-props)
      (pretty-attributes/style-attributes            table-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn table-attributes
  ; @ignore
  ;
  ; @param (keyword) table-id
  ; @param (map) table-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ table-props]
  (-> {:class :pe-table}
      (pretty-attributes/class-attributes      table-props)
      (pretty-attributes/outdent-attributes    table-props)
      (pretty-attributes/outer-size-attributes table-props)
      (pretty-attributes/state-attributes      table-props)
      (pretty-attributes/theme-attributes      table-props)))
