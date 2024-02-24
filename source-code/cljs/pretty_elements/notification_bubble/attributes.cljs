
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
  ;  ...}
  [_ bubble-props]
  (-> {:class :pe-notification-bubble--content}
      (pretty-attributes/font-attributes bubble-props)
      (pretty-attributes/text-attributes bubble-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bubble-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ bubble-props]
  (-> {:class :pe-notification-bubble--inner}
      (pretty-attributes/anchor-attributes           bubble-props)
      (pretty-attributes/background-color-attributes bubble-props)
      (pretty-attributes/border-attributes           bubble-props)
      (pretty-attributes/clickable-state-attributes  bubble-props)
      (pretty-attributes/cursor-attributes           bubble-props)
      (pretty-attributes/effect-attributes           bubble-props)
      (pretty-attributes/flex-attributes             bubble-props)
      (pretty-attributes/inner-size-attributes       bubble-props)
      (pretty-attributes/inner-space-attributes      bubble-props)
      (pretty-attributes/mouse-event-attributes      bubble-props)
      (pretty-attributes/progress-attributes         bubble-props)
      (pretty-attributes/react-attributes            bubble-props)
      (pretty-attributes/style-attributes            bubble-props)))

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
      (pretty-attributes/class-attributes          bubble-props)
      (pretty-attributes/inner-position-attributes bubble-props)
      (pretty-attributes/outer-position-attributes bubble-props)
      (pretty-attributes/outer-size-attributes     bubble-props)
      (pretty-attributes/outer-space-attributes    bubble-props)
      (pretty-attributes/state-attributes          bubble-props)
      (pretty-attributes/theme-attributes          bubble-props)))
