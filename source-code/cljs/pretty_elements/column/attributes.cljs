
(ns pretty-elements.column.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn column-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) column-id
  ; @param (map) column-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ column-props]
  (-> {:class :pe-column--inner}
      (pretty-attributes/background-color-attributes column-props)
      (pretty-attributes/border-attributes           column-props)
      (pretty-attributes/flex-attributes             column-props)
      (pretty-attributes/indent-attributes           column-props)
      (pretty-attributes/inner-size-attributes       column-props)
      (pretty-attributes/style-attributes            column-props)))

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
  (-> {:class :pe-column}
      (pretty-attributes/class-attributes          column-props)
      (pretty-attributes/inner-position-attributes column-props)
      (pretty-attributes/outdent-attributes        column-props)
      (pretty-attributes/outer-position-attributes column-props)
      (pretty-attributes/outer-size-attributes     column-props)
      (pretty-attributes/state-attributes          column-props)
      (pretty-attributes/theme-attributes          column-props)))
