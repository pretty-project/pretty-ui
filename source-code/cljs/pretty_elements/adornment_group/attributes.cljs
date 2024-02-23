
(ns pretty-elements.adornment-group.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn group-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [group-id group-props]
  (-> {:class :pe-adornment-group--inner}
      (pretty-attributes/flex-attributes       group-props)
      (pretty-attributes/indent-attributes     group-props)
      (pretty-attributes/inner-size-attributes group-props)
      (pretty-attributes/style-attributes      group-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn group-attributes
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ group-props]
  (-> {:class :pe-adornment-group}
      (pretty-attributes/class-attributes          group-props)
      (pretty-attributes/inner-position-attributes group-props)
      (pretty-attributes/outdent-attributes        group-props)
      (pretty-attributes/outer-position-attributes group-props)
      (pretty-attributes/outer-size-attributes     group-props)
      (pretty-attributes/state-attributes          group-props)
      (pretty-attributes/theme-attributes          group-props)))
