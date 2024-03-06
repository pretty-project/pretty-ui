
(ns components.vector-item-list.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn list-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) list-id
  ; @param (map) list-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ list-props]
  (-> {:class :c-vector-item-list--inner}
      (pretty-attributes/inner-space-attributes list-props)
      (pretty-attributes/style-attributes  list-props)))


;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn list-attributes
  ; @ignore
  ;
  ; @param (keyword) list-id
  ; @param (map) list-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ list-props]
  (-> {:class :c-vector-item-list}
      (pretty-attributes/class-attributes  list-props)
      (pretty-attributes/outer-space-attributes list-props)
      (pretty-attributes/state-attributes  list-props)))
