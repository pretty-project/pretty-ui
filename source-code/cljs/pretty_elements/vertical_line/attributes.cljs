
(ns pretty-elements.vertical-line.attributes
    (:require [fruits.css.api       :as css]
              [pretty-css.api :as pretty-css]))

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
      (pretty-css/color-attributes        line-props)
      (pretty-css/element-size-attributes line-props)
      (pretty-css/indent-attributes       line-props)
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
      (pretty-css/outdent-attributes      line-props)
      (pretty-css/state-attributes        line-props)
      (pretty-css/wrapper-size-attributes line-props)))
