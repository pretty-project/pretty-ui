
(ns pretty-elements.notification-bubble.attributes
    (:require [pretty-css.api :as pretty-css]
              [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.content.api :as pretty-css.content]
              [pretty-css.basic.api :as pretty-css.basic]
              [pretty-css.layout.api :as pretty-css.layout]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bubble-content-attributes
  ; @ignore
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-letter-spacing (keyword)}
  [_ bubble-props]
  (-> {:class               :pe-notification-bubble--content
       :data-letter-spacing :auto}
      (pretty-css.content/font-attributes              bubble-props)
      (pretty-css.layout/indent-attributes            bubble-props)
      (pretty-css.content/unselectable-text-attributes bubble-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bubble-body-attributes
  ; @ignore
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ bubble-props]
  (-> {:class :pe-notification-bubble--body}
      (pretty-css.appearance/background-attributes        bubble-props)
      (pretty-css.appearance/border-attributes       bubble-props)
      (pretty-css.layout/element-size-attributes bubble-props)
      (pretty-css.basic/style-attributes        bubble-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bubble-attributes
  ; @ignore
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ bubble-props]
  (-> {:class :pe-notification-bubble}
      (pretty-css.basic/class-attributes        bubble-props)
      (pretty-css.layout/outdent-attributes      bubble-props)
      (pretty-css.basic/state-attributes        bubble-props)
      (pretty-css.appearance/theme-attributes        bubble-props)
      (pretty-css.layout/wrapper-size-attributes bubble-props)))
