
(ns pretty-elements.notification-bubble.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

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
      (pretty-attributes/font-attributes   bubble-props)
      (pretty-attributes/indent-attributes bubble-props)
      (pretty-attributes/text-attributes   bubble-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bubble-body-attributes
  ; @ignore
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ bubble-props]
  (-> {:class :pe-notification-bubble--body}
      (pretty-attributes/background-attributes        bubble-props)
      (pretty-attributes/border-attributes       bubble-props)
      (pretty-attributes/anchor-attributes        bubble-props)
      (pretty-attributes/full-block-size-attributes bubble-props)
      (pretty-attributes/state-attributes        bubble-props)
      (pretty-attributes/mouse-event-attributes    bubble-props)
      (pretty-attributes/cursor-attributes       bubble-props)
      (pretty-attributes/clickable-state-attributes          bubble-props)
      (pretty-attributes/effect-attributes           bubble-props)
      (pretty-attributes/focus-attributes         bubble-props)
      (pretty-attributes/style-attributes        bubble-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bubble-attributes
  ; @ignore
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ bubble-props]
  (-> {:class :pe-notification-bubble}
      (pretty-attributes/class-attributes       bubble-props)
      (pretty-attributes/outdent-attributes      bubble-props)
      (pretty-attributes/state-attributes       bubble-props)
      (pretty-attributes/theme-attributes        bubble-props)
      (pretty-attributes/wrapper-size-attributes bubble-props)))
