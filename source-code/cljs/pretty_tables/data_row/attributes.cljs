
(ns pretty-tables.data-row.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn row-body-attributes
  ; @ignore
  ;
  ; @param (keyword) row-id
  ; @param (map) row-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ row-props]
  (-> {:class :pt-data-row--body}
      (pretty-attributes/double-block-size-attributes row-props)
      (pretty-attributes/grid-attributes              row-props)
      (pretty-attributes/indent-attributes            row-props)
      (pretty-attributes/style-attributes             row-props)))

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
  (-> {:class :pt-data-row}
      (pretty-attributes/class-attributes        row-props)
      (pretty-attributes/outdent-attributes      row-props)
      (pretty-attributes/state-attributes        row-props)
      (pretty-attributes/theme-attributes        row-props)
      (pretty-attributes/wrapper-size-attributes row-props)))
