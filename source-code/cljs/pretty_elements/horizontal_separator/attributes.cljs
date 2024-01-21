
(ns pretty-elements.horizontal-separator.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn separator-body-attributes
  ; @ignore
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ separator-props]
  (-> {:class :pe-horizontal-separator--body}
      (pretty-build-kit/color-attributes        separator-props)
      (pretty-build-kit/indent-attributes       separator-props)
      (pretty-build-kit/element-size-attributes separator-props)
      (pretty-build-kit/style-attributes        separator-props)
      (pretty-build-kit/unselectable-attributes separator-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn separator-attributes
  ; @ignore
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ separator-props]
  (-> {:class :pe-horizontal-separator}
      (pretty-build-kit/class-attributes        separator-props)
      (pretty-build-kit/outdent-attributes      separator-props)
      (pretty-build-kit/state-attributes        separator-props)
      (pretty-build-kit/wrapper-size-attributes separator-props)))
