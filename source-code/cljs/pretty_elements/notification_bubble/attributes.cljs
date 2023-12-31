
(ns pretty-elements.notification-bubble.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

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
      (pretty-build-kit/font-attributes   bubble-props)
      (pretty-build-kit/indent-attributes bubble-props)))

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
      (pretty-build-kit/border-attributes           bubble-props)
      (pretty-build-kit/color-attributes            bubble-props)
      (pretty-build-kit/element-max-size-attributes bubble-props)
      (pretty-build-kit/element-min-size-attributes bubble-props)
      (pretty-build-kit/element-size-attributes     bubble-props)))

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
      (pretty-build-kit/class-attributes        bubble-props)
      (pretty-build-kit/outdent-attributes      bubble-props)
      (pretty-build-kit/state-attributes        bubble-props)
      (pretty-build-kit/wrapper-size-attributes bubble-props)))
