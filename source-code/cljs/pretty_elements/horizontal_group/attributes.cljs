
(ns pretty-elements.horizontal-group.attributes
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
  (-> {:class :pe-horizontal-group--body}
      (pretty-css/indent-attributes       group-props)
      (pretty-css/element-size-attributes group-props)
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
  (-> {:class :pe-horizontal-group}
      (pretty-css/class-attributes        group-props)
      (pretty-css/outdent-attributes      group-props)
      (pretty-css/state-attributes        group-props)
      (pretty-css/wrapper-size-attributes group-props)))
