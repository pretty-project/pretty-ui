
(ns pretty-elements.adornment-group.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adornment-group-body-attributes
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  ; {}
  [group-id group-props]
  (-> {:class :pe-adornment-group--body}
      (pretty-build-kit/indent-attributes group-props)
      (pretty-build-kit/style-attributes  group-props)))

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
      (pretty-build-kit/class-attributes   group-props)
      (pretty-build-kit/outdent-attributes group-props)
      (pretty-build-kit/state-attributes   group-props)))
