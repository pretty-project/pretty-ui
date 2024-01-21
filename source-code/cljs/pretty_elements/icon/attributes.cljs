
(ns pretty-elements.icon.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn icon-body-attributes
  ; @ignore
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  ;
  ; @return (map)
  ; {}
  [icon-id icon-props]
  (-> {:class :pe-icon--body}
      (pretty-build-kit/icon-attributes         icon-props)
      (pretty-build-kit/indent-attributes       icon-props)
      (pretty-build-kit/style-attributes        icon-props)
      (pretty-build-kit/unselectable-attributes icon-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn icon-attributes
  ; @ignore
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  ;
  ; @return (map)
  ; {}
  [_ icon-props]
  (-> {:class :pe-icon}
      (pretty-build-kit/class-attributes   icon-props)
      (pretty-build-kit/outdent-attributes icon-props)
      (pretty-build-kit/state-attributes   icon-props)))
