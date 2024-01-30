
(ns pretty-elements.chip.attributes
    (:require [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.basic.api      :as pretty-css.basic]
              [pretty-css.content.api    :as pretty-css.content]
              [pretty-css.control.api    :as pretty-css.control]
              [pretty-css.layout.api     :as pretty-css.layout]
              [pretty-css.live.api       :as pretty-css.live]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-label-attributes
  ; @ignore
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ chip-props]
  (-> {:class :pe-chip--label}
      (pretty-css.content/font-attributes              chip-props)
      (pretty-css.content/unselectable-text-attributes chip-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-body-attributes
  ; @ignore
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [chip-id chip-props]
  (-> {:class :pe-chip--body}
      (pretty-css.appearance/background-attributes chip-props)
      (pretty-css.appearance/border-attributes     chip-props)
      (pretty-css.live/effect-attributes       chip-props)
      (pretty-css.layout/element-size-attributes chip-props)
      (pretty-css.control/anchor-attributes         chip-props)
      (pretty-css.control/state-attributes         chip-props)
      (pretty-css.content/cursor-attributes       chip-props)
      (pretty-css.layout/indent-attributes       chip-props)
      (pretty-css.control/focus-attributes         chip-props)
      (pretty-css.control/mouse-event-attributes  chip-props)
      (pretty-css.basic/style-attributes        chip-props)
      (pretty-css.control/tab-attributes           chip-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-attributes
  ; @ignore
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ chip-props]
  (-> {:class :pe-chip}
      (pretty-css.basic/class-attributes        chip-props)
      (pretty-css.layout/outdent-attributes      chip-props)
      (pretty-css.basic/state-attributes        chip-props)
      (pretty-css.appearance/theme-attributes        chip-props)
      (pretty-css.layout/wrapper-size-attributes chip-props)))
