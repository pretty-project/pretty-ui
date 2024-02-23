
(ns pretty-elements.chip.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-label-attributes
  ; @ignore
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ chip-props]
  (-> {:class :pe-chip--label}
      (pretty-attributes/font-attributes chip-props)
      (pretty-attributes/text-attributes chip-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [chip-id chip-props]
  (-> {:class :pe-chip--inner}
      (pretty-attributes/anchor-attributes           chip-props)
      (pretty-attributes/background-color-attributes chip-props)
      (pretty-attributes/border-attributes           chip-props)
      (pretty-attributes/clickable-state-attributes  chip-props)
      (pretty-attributes/cursor-attributes           chip-props)
      (pretty-attributes/effect-attributes           chip-props)
      (pretty-attributes/flex-attributes             chip-props)
      (pretty-attributes/indent-attributes           chip-props)
      (pretty-attributes/inner-size-attributes       chip-props)
      (pretty-attributes/mouse-event-attributes      chip-props)
      (pretty-attributes/react-attributes            chip-props)
      (pretty-attributes/style-attributes            chip-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-attributes
  ; @ignore
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ chip-props]
  (-> {:class :pe-chip}
      (pretty-attributes/class-attributes          chip-props)
      (pretty-attributes/inner-position-attributes chip-props)
      (pretty-attributes/outdent-attributes        chip-props)
      (pretty-attributes/outer-position-attributes chip-props)
      (pretty-attributes/outer-size-attributes     chip-props)
      (pretty-attributes/state-attributes          chip-props)
      (pretty-attributes/theme-attributes          chip-props)))
