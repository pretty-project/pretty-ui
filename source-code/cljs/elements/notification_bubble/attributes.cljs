
(ns elements.notification-bubble.attributes
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bubble-content-attributes
  ; @ignore
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-letter-spacing (keyword)
  ;  :style (map)}
  [_ {:keys [style] :as bubble-props}]
  (-> {:class               :pe-notification-bubble--content
       :style               style
       :data-letter-spacing :auto}
      (pretty-css/font-attributes   bubble-props)
      (pretty-css/indent-attributes bubble-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bubble-body-attributes
  ; @ignore
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ; {:selectable? (boolean)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-selectable (boolean)}
  [_ {:keys [selectable?] :as bubble-props}]
  (-> {:class           :pe-notification-bubble--body
       :data-selectable selectable?}
      (pretty-css/border-attributes bubble-props)
      (pretty-css/color-attributes  bubble-props)))

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
      (pretty-css/default-attributes          bubble-props)
      (pretty-css/outdent-attributes          bubble-props)
      (pretty-css/element-max-size-attributes bubble-props)
      (pretty-css/element-min-size-attributes bubble-props)
      (pretty-css/element-size-attributes     bubble-props)))
