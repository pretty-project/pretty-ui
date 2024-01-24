
(ns pretty-elements.horizontal-separator.attributes
    (:require [pretty-css.api :as pretty-css]))

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
      (pretty-css/color-attributes             separator-props)
      (pretty-css/indent-attributes            separator-props)
      (pretty-css/element-size-attributes      separator-props)
      (pretty-css/style-attributes             separator-props)
      (pretty-css/unselectable-text-attributes separator-props)))

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
      (pretty-css/class-attributes        separator-props)
      (pretty-css/outdent-attributes      separator-props)
      (pretty-css/state-attributes        separator-props)
      (pretty-css/wrapper-size-attributes separator-props)))
