
(ns components.vector-item-list.attributes
    (:require [pretty-css.api :as pretty-css]
              [pretty-css.layout.api :as pretty-css.layout]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn list-body-attributes
  ; @ignore
  ;
  ; @param (keyword) list-id
  ; @param (map) list-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ list-props]
  (-> {:class :c-vector-item-list--body}
      (pretty-css.layout/indent-attributes list-props)
      (pretty-css/style-attributes  list-props)))


;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn list-attributes
  ; @ignore
  ;
  ; @param (keyword) list-id
  ; @param (map) list-props
  ;
  ; @return (map)
  ; {}
  [_ list-props]
  (-> {:class :c-vector-item-list}
      (pretty-css/class-attributes   list-props)
      (pretty-css.layout/outdent-attributes list-props)
      (pretty-css/state-attributes   list-props)))
