
(ns pretty-elements.horizontal-separator.attributes
    (:require [pretty-css.api :as pretty-css]
              [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.layout.api :as pretty-css.layout]))

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
      (pretty-css.appearance/background-attributes             separator-props)
      (pretty-css.layout/indent-attributes            separator-props)
      (pretty-css.layout/element-size-attributes      separator-props)
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
      (pretty-css.layout/outdent-attributes      separator-props)
      (pretty-css/state-attributes        separator-props)
      (pretty-css/theme-attributes        separator-props)
      (pretty-css.layout/wrapper-size-attributes separator-props)))
