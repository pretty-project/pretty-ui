
(ns components.vector-item-list.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn list-body-attributes
  ; @ignore
  ;
  ; @param (keyword) list-id
  ; @param (map) list-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)}
  [_ {:keys [style] :as list-props}]
  (-> {:class :c-vector-item-list--body
       :style style}
      (pretty-build-kit/indent-attributes list-props)))

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
      (pretty-build-kit/class-attributes   list-props)
      (pretty-build-kit/outdent-attributes list-props)
      (pretty-build-kit/state-attributes   list-props)))
