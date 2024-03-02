
(ns pretty-elements.crumb-group.attributes
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
  [_ group-props]
  (-> {:class :pe-crumb-group--inner}
      (pretty-attributes/flex-attributes        group-props)
      (pretty-attributes/inner-size-attributes  group-props)
      (pretty-attributes/inner-space-attributes group-props)
      (pretty-attributes/style-attributes       group-props)))

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
  (-> {:class :pe-crumb-group}
      (pretty-attributes/class-attributes          group-props)
      (pretty-attributes/inner-position-attributes group-props)
      (pretty-attributes/outer-position-attributes group-props)
      (pretty-attributes/outer-size-attributes     group-props)
      (pretty-attributes/outer-space-attributes    group-props)
      (pretty-attributes/state-attributes          group-props)
      (pretty-attributes/theme-attributes          group-props)))