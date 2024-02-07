
(ns pretty-tables.data-table.attributes
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
  (-> {:class :pe-data-table--body}
      (pretty-attributes/double-block-size-attributes table-props)
      (pretty-attributes/indent-attributes            table-props)
      (pretty-attributes/style-attributes             table-props)))

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
  (-> {:class :pe-data-table}
      (pretty-attributes/class-attributes        table-props)
      (pretty-attributes/outdent-attributes      table-props)
      (pretty-attributes/state-attributes        table-props)
      (pretty-attributes/theme-attributes        table-props)
      (pretty-attributes/wrapper-size-attributes table-props)))
