
(ns pretty-elements.chip.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

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
  [_ _]
  {:class               :pe-chip--label
   :data-font-size      :xs
   :data-font-weight    :medium
   :data-letter-spacing :auto
   :data-line-height    :text-block
   :data-text-overflow  :hidden})

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
      (pretty-build-kit/border-attributes           chip-props)
      (pretty-build-kit/color-attributes            chip-props)
      (pretty-build-kit/effect-attributes           chip-props)
      (pretty-build-kit/element-min-size-attributes chip-props)
      (pretty-build-kit/element-size-attributes     chip-props)
      (pretty-build-kit/href-attributes             chip-props)
      (pretty-build-kit/indent-attributes           chip-props)
      (pretty-build-kit/mouse-event-attributes      chip-props)
      (pretty-build-kit/style-attributes            chip-props)
      (pretty-build-kit/unselectable-attributes     chip-props)))

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
      (pretty-build-kit/class-attributes        chip-props)
      (pretty-build-kit/outdent-attributes      chip-props)
      (pretty-build-kit/state-attributes        chip-props)
      (pretty-build-kit/wrapper-size-attributes chip-props)))
