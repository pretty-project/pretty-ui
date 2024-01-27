
(ns pretty-elements.column.attributes
    (:require [pretty-css.api :as pretty-css]
              [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.layout.api :as pretty-css.layout]))

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
      (pretty-css.appearance/background-attributes        column-props)
      (pretty-css.appearance/border-attributes       column-props)
      (pretty-css/column-attributes       column-props)
      (pretty-css.layout/element-size-attributes column-props)
      (pretty-css.layout/indent-attributes       column-props)
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
      (pretty-css.layout/outdent-attributes      column-props)
      (pretty-css/state-attributes        column-props)
      (pretty-css/theme-attributes        column-props)
      (pretty-css.layout/wrapper-size-attributes column-props)))
