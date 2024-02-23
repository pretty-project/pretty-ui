
(ns pretty-tables.row.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn row-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) row-id
  ; @param (map) row-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ row-props]
  (-> {:class :pt-row--inner}
      (pretty-attributes/background-color-attributes row-props)
      (pretty-attributes/border-attributes           row-props)
      (pretty-attributes/grid-attributes             row-props)
      (pretty-attributes/indent-attributes           row-props)
      (pretty-attributes/inner-size-attributes       row-props)
      (pretty-attributes/style-attributes            row-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn row-attributes
  ; @ignore
  ;
  ; @param (keyword) row-id
  ; @param (map) row-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ row-props]
  (-> {:class :pt-row}
      (pretty-attributes/class-attributes          row-props)
      (pretty-attributes/inner-position-attributes row-props)
      (pretty-attributes/outdent-attributes        row-props)
      (pretty-attributes/outer-position-attributes row-props)
      (pretty-attributes/outer-size-attributes     row-props)
      (pretty-attributes/state-attributes          row-props)
      (pretty-attributes/theme-attributes          row-props)))
