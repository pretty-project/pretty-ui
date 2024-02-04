
(ns pretty-elements.horizontal-separator.attributes
    (:require [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.basic.api      :as pretty-css.basic]
              [pretty-css.content.api    :as pretty-css.content]
              [pretty-css.layout.api     :as pretty-css.layout]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn separator-body-attributes
  ; @ignore
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ separator-props]
  (-> {:class :pe-horizontal-separator--body}
      (pretty-css.appearance/background-attributes             separator-props)
      (pretty-css.layout/indent-attributes            separator-props)
      (pretty-css.layout/full-block-size-attributes      separator-props)
      (pretty-css.basic/style-attributes             separator-props)
      (pretty-css.content/unselectable-text-attributes separator-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn separator-attributes
  ; @ignore
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ separator-props]
  (-> {:class :pe-horizontal-separator}
      (pretty-css.basic/class-attributes        separator-props)
      (pretty-css.layout/outdent-attributes      separator-props)
      (pretty-css.basic/state-attributes        separator-props)
      (pretty-css.appearance/theme-attributes        separator-props)
      (pretty-css.layout/wrapper-size-attributes separator-props)))
