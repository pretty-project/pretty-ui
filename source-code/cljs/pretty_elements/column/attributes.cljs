
(ns pretty-elements.column.attributes
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn column-body-attributes
  ; @ignore
  ;
  ; @param (keyword) column-id
  ; @param (map) column-props
  ;
  ; @return (map)
  ; {}
  [_ column-props]
  (-> {:class :pe-column--body}
      (pretty-css/border-attributes       column-props)
      (pretty-css/color-attributes        column-props)
      (pretty-css/column-attributes       column-props)
      (pretty-css/element-size-attributes column-props)
      (pretty-css/indent-attributes       column-props)
      (pretty-css/style-attributes        column-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn column-attributes
  ; @ignore
  ;
  ; @param (keyword) column-id
  ; @param (map) column-props
  ;
  ; @return (map)
  ; {}
  [_ column-props]
  (-> {:class :pe-column}
      (pretty-css/class-attributes        column-props)
      (pretty-css/outdent-attributes      column-props)
      (pretty-css/state-attributes        column-props)
      (pretty-css/wrapper-size-attributes column-props)))
