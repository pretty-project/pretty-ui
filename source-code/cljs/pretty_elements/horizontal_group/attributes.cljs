
(ns pretty-elements.horizontal-group.attributes
    (:require [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.basic.api      :as pretty-css.basic]
              [pretty-css.layout.api     :as pretty-css.layout]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn group-body-attributes
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ group-props]
  (-> {:class :pe-horizontal-group--body}
      (pretty-css.layout/indent-attributes       group-props)
      (pretty-css.layout/element-size-attributes group-props)
      (pretty-css.basic/style-attributes        group-props)))

(defn group-attributes
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  ; {}
  [_ group-props]
  (-> {:class :pe-horizontal-group}
      (pretty-css.basic/class-attributes        group-props)
      (pretty-css.layout/outdent-attributes      group-props)
      (pretty-css.basic/state-attributes        group-props)
      (pretty-css.appearance/theme-attributes        group-props)
      (pretty-css.layout/wrapper-size-attributes group-props)))
