
(ns pretty-elements.horizontal-line.attributes
    (:require [fruits.css.api            :as css]
              [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.basic.api      :as pretty-css.basic]
              [pretty-css.layout.api     :as pretty-css.layout]))

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
  (-> {:class :pe-horizontal-line--body
       :style {:height (css/px strength)}}
      (pretty-css.appearance/background-attributes        line-props)
      (pretty-css.layout/full-block-size-attributes line-props)
      (pretty-css.layout/indent-attributes       line-props)
      (pretty-css.basic/style-attributes        line-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn line-attributes
  ; @ignore
  ;
  ; @param (keyword) line-id
  ; @param (map) line-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ line-props]
  (-> {:class :pe-horizontal-line}
      (pretty-css.basic/class-attributes        line-props)
      (pretty-css.layout/outdent-attributes      line-props)
      (pretty-css.basic/state-attributes        line-props)
      (pretty-css.appearance/theme-attributes        line-props)
      (pretty-css.layout/wrapper-size-attributes line-props)))
