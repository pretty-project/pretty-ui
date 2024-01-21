
(ns pretty-elements.column.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

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
      (pretty-build-kit/border-attributes           column-props)
      (pretty-build-kit/color-attributes            column-props)
      (pretty-build-kit/column-attributes           column-props)
      (pretty-build-kit/element-max-size-attributes column-props)
      (pretty-build-kit/element-min-size-attributes column-props)
      (pretty-build-kit/element-size-attributes     column-props)
      (pretty-build-kit/indent-attributes           column-props)
      (pretty-build-kit/style-attributes            column-props)))

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
      (pretty-build-kit/class-attributes        column-props)
      (pretty-build-kit/outdent-attributes      column-props)
      (pretty-build-kit/state-attributes        column-props)
      (pretty-build-kit/wrapper-size-attributes column-props)))
