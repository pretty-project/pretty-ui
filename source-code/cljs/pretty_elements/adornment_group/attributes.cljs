
(ns pretty-elements.adornment-group.attributes
    (:require [pretty-css.api :as pretty-css]
              [pretty-css.layout.api :as pretty-css.layout]))

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
      (pretty-css.layout/element-size-attributes group-props)
      (pretty-css.layout/indent-attributes       group-props)
      (pretty-css/flex-attributes         group-props)
      (pretty-css/style-attributes        group-props)))

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
      (pretty-css/class-attributes        group-props)
      (pretty-css.layout/outdent-attributes      group-props)
      (pretty-css/state-attributes        group-props)
      (pretty-css/theme-attributes        group-props)
      (pretty-css.layout/wrapper-size-attributes group-props)))
