
(ns pretty-elements.notification-bubble.attributes
    (:require [pretty-css.api :as pretty-css]))

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
      (pretty-css/font-attributes              bubble-props)
      (pretty-css/indent-attributes            bubble-props)
      (pretty-css/unselectable-text-attributes bubble-props)))

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
      (pretty-css/border-attributes       bubble-props)
      (pretty-css/color-attributes        bubble-props)
      (pretty-css/element-size-attributes bubble-props)
      (pretty-css/style-attributes        bubble-props)))

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
      (pretty-css/class-attributes        bubble-props)
      (pretty-css/outdent-attributes      bubble-props)
      (pretty-css/state-attributes        bubble-props)
      (pretty-css/wrapper-size-attributes bubble-props)))
