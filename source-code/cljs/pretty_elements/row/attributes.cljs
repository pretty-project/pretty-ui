
(ns pretty-elements.row.attributes
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn row-body-attributes
  ; @ignore
  ;
  ; @param (keyword) row-id
  ; @param (map) row-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ row-props]
  (-> {:class :pe-row--body}
      (pretty-css/border-attributes           row-props)
      (pretty-css/color-attributes            row-props)
      (pretty-css/indent-attributes           row-props)
      (pretty-css/element-max-size-attributes row-props)
      (pretty-css/element-min-size-attributes row-props)
      (pretty-css/element-size-attributes     row-props)
      (pretty-css/row-attributes              row-props)
      (pretty-css/style-attributes            row-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn row-attributes
  ; @ignore
  ;
  ; @param (keyword) row-id
  ; @param (map) row-props
  ;
  ; @return (map)
  ; {}
  [_ row-props]
  (-> {:class :pe-row}
      (pretty-css/class-attributes        row-props)
      (pretty-css/outdent-attributes      row-props)
      (pretty-css/state-attributes        row-props)
      (pretty-css/wrapper-size-attributes row-props)))
