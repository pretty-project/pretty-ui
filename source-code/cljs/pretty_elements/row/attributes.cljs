
(ns pretty-elements.row.attributes
    (:require [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.basic.api      :as pretty-css.basic]
              [pretty-css.layout.api     :as pretty-css.layout]))

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
  (-> {:class :pe-row--body}
      (pretty-css.appearance/background-attributes        row-props)
      (pretty-css.appearance/border-attributes       row-props)
      (pretty-css.layout/indent-attributes       row-props)
      (pretty-css.layout/double-block-size-attributes row-props)
      (pretty-css.layout/flex-attributes          row-props)
      (pretty-css.basic/style-attributes        row-props)))

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
      (pretty-css.basic/class-attributes        row-props)
      (pretty-css.layout/outdent-attributes      row-props)
      (pretty-css.basic/state-attributes        row-props)
      (pretty-css.appearance/theme-attributes        row-props)
      (pretty-css.layout/wrapper-size-attributes row-props)))
