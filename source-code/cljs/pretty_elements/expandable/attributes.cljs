
(ns pretty-elements.expandable.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expandable-body-attributes
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ expandable-props]
  (-> {:class :pe-expandable--body}
      (pretty-attributes/indent-attributes expandable-props)
      (pretty-attributes/style-attributes  expandable-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expandable-attributes
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ expandable-props]
  (-> {:class :pe-expandable}
      (pretty-attributes/class-attributes   expandable-props)
      (pretty-attributes/state-attributes   expandable-props)
      (pretty-attributes/outdent-attributes expandable-props)
      (pretty-attributes/theme-attributes   expandable-props)))
