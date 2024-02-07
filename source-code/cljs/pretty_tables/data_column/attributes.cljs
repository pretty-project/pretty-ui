
(ns pretty-tables.data-column.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

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
      (pretty-attributes/grid-attributes              column-props)
      (pretty-attributes/double-block-size-attributes column-props)
      (pretty-attributes/indent-attributes            column-props)
      (pretty-attributes/style-attributes             column-props)))

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
      (pretty-attributes/class-attributes        column-props)
      (pretty-attributes/state-attributes        column-props)
      (pretty-attributes/outdent-attributes      column-props)
      (pretty-attributes/theme-attributes        column-props)
      (pretty-attributes/wrapper-size-attributes column-props)))
