
(ns pretty-elements.chip.attributes
    (:require [pretty-css.api :as pretty-css]))

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
      (pretty-css/border-attributes           chip-props)
      (pretty-css/color-attributes            chip-props)
      (pretty-css/effect-attributes           chip-props)
      (pretty-css/element-min-size-attributes chip-props)
      (pretty-css/element-size-attributes     chip-props)
      (pretty-css/href-attributes             chip-props)
      (pretty-css/indent-attributes           chip-props)
      (pretty-css/mouse-event-attributes      chip-props)
      (pretty-css/style-attributes            chip-props)))

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
      (pretty-css/outdent-attributes      chip-props)
      (pretty-css/state-attributes        chip-props)
      (pretty-css/wrapper-size-attributes chip-props)))
