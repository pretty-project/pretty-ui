
(ns pretty-elements.chip.attributes
    (:require [pretty-css.api :as pretty-css]
              [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.layout.api :as pretty-css.layout]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-label-attributes
  ; @ignore
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ;
  ; @return (map)
  ; {}
  [_ chip-props]
  (-> {:class               :pe-chip--label
       :data-font-size      :xs
       :data-font-weight    :medium
       :data-letter-spacing :auto
       :data-line-height    :text-block
       :data-text-overflow  :hidden}
      (pretty-css/unselectable-text-attributes chip-props)))

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
      (pretty-css/effect-attributes       chip-props)
      (pretty-css.layout/element-size-attributes chip-props)
      (pretty-css/href-attributes         chip-props)
      (pretty-css.layout/indent-attributes       chip-props)
      (pretty-css/mouse-event-attributes  chip-props)
      (pretty-css/style-attributes        chip-props)))

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
      (pretty-css/class-attributes        chip-props)
      (pretty-css.layout/outdent-attributes      chip-props)
      (pretty-css/state-attributes        chip-props)
      (pretty-css/theme-attributes        chip-props)
      (pretty-css.layout/wrapper-size-attributes chip-props)))
