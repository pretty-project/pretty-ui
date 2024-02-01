
(ns pretty-elements.adornment-group.attributes
    (:require [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.basic.api      :as pretty-css.basic]
              [pretty-css.layout.api     :as pretty-css.layout]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adornment-group-body-attributes
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [group-id group-props]
  (-> {:class :pe-adornment-group--body}
      (pretty-css.basic/style-attributes            group-props)
      (pretty-css.layout/full-block-size-attributes group-props)
      (pretty-css.layout/indent-attributes          group-props)
      (pretty-css.layout/flex-attributes            group-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adornment-group-attributes
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ group-props]
  (-> {:class :pe-adornment-group}
      (pretty-css.appearance/theme-attributes    group-props)
      (pretty-css.basic/class-attributes         group-props)
      (pretty-css.basic/state-attributes         group-props)
      (pretty-css.layout/outdent-attributes      group-props)
      (pretty-css.layout/wrapper-size-attributes group-props)))
