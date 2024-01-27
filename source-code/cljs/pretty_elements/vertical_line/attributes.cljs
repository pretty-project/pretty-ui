
(ns pretty-elements.vertical-line.attributes
    (:require [fruits.css.api       :as css]
              [pretty-css.api :as pretty-css]
              [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.layout.api :as pretty-css.layout]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn line-body-attributes
  ; @ignore
  ;
  ; @param (keyword) line-id
  ; @param (map) line-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [line-id {:keys [strength] :as line-props}]
  (-> {:class :pe-vertical-line--body
       :style {:width (css/px strength)}}
      (pretty-css.appearance/background-attributes        line-props)
      (pretty-css.layout/element-size-attributes line-props)
      (pretty-css.layout/indent-attributes       line-props)
      (pretty-css/style-attributes        line-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn line-attributes
  ; @ignore
  ;
  ; @param (keyword) line-id
  ; @param (map) line-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ line-props]
  (-> {:class :pe-vertical-line}
      (pretty-css/class-attributes        line-props)
      (pretty-css.layout/outdent-attributes      line-props)
      (pretty-css/state-attributes        line-props)
      (pretty-css/theme-attributes        line-props)
      (pretty-css.layout/wrapper-size-attributes line-props)))
