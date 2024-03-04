
(ns pretty-elements.image.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn image-canvas-attributes
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ props]
  (-> {:class :pe-image--canvas}
      (pretty-attributes/background-image-attributes props)
      (pretty-attributes/content-size-attributes     props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn inner-attributes
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ props]
  (-> {:class :pe-image--inner}
      (pretty-attributes/anchor-attributes            props)
      (pretty-attributes/animation-attributes         props)
      (pretty-attributes/background-action-attributes props)
      (pretty-attributes/background-color-attributes  props)
      (pretty-attributes/border-attributes            props)
      (pretty-attributes/clickable-state-attributes   props)
      (pretty-attributes/cursor-attributes            props)
      (pretty-attributes/flex-attributes              props)
      (pretty-attributes/effect-attributes            props)
      (pretty-attributes/inner-size-attributes        props)
      (pretty-attributes/inner-space-attributes       props)
      (pretty-attributes/mouse-event-attributes       props)
      (pretty-attributes/react-attributes             props)
      (pretty-attributes/style-attributes             props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn outer-attributes
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ props]
  (-> {:class :pe-image--outer}
      (pretty-attributes/class-attributes          props)
      (pretty-attributes/inner-position-attributes props)
      (pretty-attributes/outer-position-attributes props)
      (pretty-attributes/outer-size-attributes     props)
      (pretty-attributes/outer-space-attributes    props)
      (pretty-attributes/state-attributes          props)
      (pretty-attributes/theme-attributes          props)))
