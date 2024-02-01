
(ns pretty-elements.label.attributes
    (:require [pretty-css.appearance.api          :as pretty-css.appearance]
              [pretty-css.basic.api               :as pretty-css.basic]
              [pretty-css.content.api             :as pretty-css.content]
              [pretty-css.layout.api              :as pretty-css.layout]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-icon-attributes
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ label-props]
  (-> {:class :pe-label--icon}
      (pretty-css.content/icon-attributes label-props)))

(defn label-content-attributes
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ label-props]
  (-> {:class :pe-label--content}
      (pretty-css.content/font-attributes              label-props)
      (pretty-css.content/unselectable-text-attributes label-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-body-attributes
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ label-props]
  (-> {:class :pe-label--body}
      (pretty-css.appearance/background-attributes label-props)
      (pretty-css.appearance/border-attributes     label-props)
      (pretty-css.layout/element-size-attributes   label-props)
      (pretty-css.layout/flex-attributes           label-props)
      (pretty-css.layout/indent-attributes         label-props)
      (pretty-css.basic/style-attributes           label-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-attributes
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ label-props]
  (-> {:class :pe-label}
      (pretty-css.appearance/theme-attributes    label-props)
      (pretty-css.basic/class-attributes         label-props)
      (pretty-css.basic/state-attributes         label-props)
      (pretty-css.layout/outdent-attributes      label-props)
      (pretty-css.layout/wrapper-size-attributes label-props)))
