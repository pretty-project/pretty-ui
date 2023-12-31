
(ns pretty-elements.horizontal-group.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn group-body-attributes
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)}
  [_ {:keys [style] :as group-props}]
  (-> {:class :pe-horizontal-group--body
       :style style}
      (pretty-build-kit/indent-attributes       group-props)
      (pretty-build-kit/element-size-attributes group-props)))

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
      (pretty-build-kit/class-attributes        group-props)
      (pretty-build-kit/outdent-attributes      group-props)
      (pretty-build-kit/state-attributes        group-props)
      (pretty-build-kit/wrapper-size-attributes group-props)))
