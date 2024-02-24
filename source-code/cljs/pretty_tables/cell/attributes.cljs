
(ns pretty-tables.cell.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn cell-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) cell-id
  ; @param (map) cell-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ cell-props]
  (-> {:class :pt-cell--inner}
      (pretty-attributes/background-color-attributes cell-props)
      (pretty-attributes/border-attributes           cell-props)
      (pretty-attributes/flex-attributes             cell-props)
      (pretty-attributes/font-attributes             cell-props)
      (pretty-attributes/inner-size-attributes       cell-props)
      (pretty-attributes/inner-space-attributes      cell-props)
      (pretty-attributes/style-attributes            cell-props)
      (pretty-attributes/text-attributes             cell-props)))

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
  (-> {:class :pt-cell}
      (pretty-attributes/class-attributes          cell-props)
      (pretty-attributes/inner-position-attributes cell-props)
      (pretty-attributes/outer-position-attributes cell-props)
      (pretty-attributes/outer-size-attributes     cell-props)
      (pretty-attributes/outer-space-attributes    cell-props)
      (pretty-attributes/state-attributes          cell-props)
      (pretty-attributes/theme-attributes          cell-props)))
