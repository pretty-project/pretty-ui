
(ns pretty-elements.image.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn image-canvas-attributes
  ; @ignore
  ;
  ; @param (keyword) image-id
  ; @param (map) image-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ image-props]
  (-> {:class :pe-image--canvas}
      (pretty-attributes/background-image-attributes image-props)
      (pretty-attributes/canvas-attributes           image-props)))

(defn image-icon-attributes
  ; @ignore
  ;
  ; @param (keyword) image-id
  ; @param (map) image-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ image-props]
  (-> {:class :pe-image--icon}
      (pretty-attributes/animation-attributes image-props)
      (pretty-attributes/canvas-attributes    image-props)
      (pretty-attributes/icon-attributes      image-props)))

(defn image-label-attributes
  ; @ignore
  ;
  ; @param (keyword) image-id
  ; @param (map) image-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ image-props]
  (-> {:class :pe-image--label}
      (pretty-attributes/font-attributes image-props)
      (pretty-attributes/text-attributes image-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn image-body-attributes
  ; @ignore
  ;
  ; @param (keyword) image-id
  ; @param (map) image-props
  ; {}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ image-props]
  (-> {:class :pe-image--body}
      (pretty-attributes/anchor-attributes            image-props)
      (pretty-attributes/background-color-attributes  image-props)
      (pretty-attributes/border-attributes            image-props)
      (pretty-attributes/clickable-state-attributes   image-props)
      (pretty-attributes/cursor-attributes            image-props)
      (pretty-attributes/flex-attributes              image-props)
      (pretty-attributes/double-block-size-attributes image-props)
      (pretty-attributes/effect-attributes            image-props)
      (pretty-attributes/indent-attributes            image-props)
      (pretty-attributes/mouse-event-attributes       image-props)
      (pretty-attributes/react-attributes             image-props)
      (pretty-attributes/style-attributes             image-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn image-attributes
  ; @ignore
  ;
  ; @param (keyword) image-id
  ; @param (map) image-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ image-props]
  (-> {:class :pe-image}
      (pretty-attributes/class-attributes        image-props)
      (pretty-attributes/outdent-attributes      image-props)
      (pretty-attributes/state-attributes        image-props)
      (pretty-attributes/theme-attributes        image-props)
      (pretty-attributes/wrapper-size-attributes image-props)))
