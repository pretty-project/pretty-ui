
(ns pretty-elements.notification-bubble.attributes
    (:require [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.basic.api      :as pretty-css.basic]
              [pretty-css.content.api    :as pretty-css.content]
              [pretty-css.layout.api     :as pretty-css.layout]
              [pretty-css.live.api :as pretty-css.live]
              [pretty-css.control.api    :as pretty-css.control]))

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
      (pretty-css.control/anchor-attributes        bubble-props)
      (pretty-css.layout/element-size-attributes bubble-props)
      (pretty-css.control/state-attributes         bubble-props)
      (pretty-css.control/mouse-event-attributes    bubble-props)
      (pretty-css.content/cursor-attributes       bubble-props)
      (pretty-css.control/tab-attributes           bubble-props)
      (pretty-css.live/effect-attributes           bubble-props)
      (pretty-css.control/focus-attributes         bubble-props)
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
