
(ns pretty-elements.vertical-group.attributes
    (:require [pretty-css.api :as pretty-css]))

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
  (-> {:class :pe-vertical-group--body}
      (pretty-css/element-size-attributes group-props)
      (pretty-css/indent-attributes       group-props)
      (pretty-css/style-attributes        group-props)))

(defn group-attributes
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  ; {}
  [_ group-props]
  (-> {:class :pe-vertical-group}
      (pretty-css/class-attributes        group-props)
      (pretty-css/outdent-attributes      group-props)
      (pretty-css/state-attributes        group-props)
      (pretty-css/wrapper-size-attributes group-props)))
