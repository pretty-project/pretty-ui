
(ns pretty-elements.thumbnail.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn thumbnail-canvas-attributes
  ; @ignore
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ thumbnail-props]
  (-> {:class :pe-thumbnail--canvas}
      (pretty-attributes/background-image-attributes thumbnail-props)
      (pretty-attributes/canvas-attributes           thumbnail-props)))

(defn thumbnail-icon-attributes
  ; @ignore
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ thumbnail-props]
  (-> {:class :pe-thumbnail--icon}
      (pretty-attributes/animation-attributes thumbnail-props)
      (pretty-attributes/canvas-attributes    thumbnail-props)
      (pretty-attributes/icon-attributes      thumbnail-props)))

(defn thumbnail-label-attributes
  ; @ignore
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ thumbnail-props]
  (-> {:class :pe-thumbnail--label}
      (pretty-attributes/font-attributes thumbnail-props)
      (pretty-attributes/text-attributes thumbnail-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn thumbnail-body-attributes
  ; @ignore
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ; {}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ thumbnail-props]
  (-> {:class :pe-thumbnail--body}
      (pretty-attributes/anchor-attributes           thumbnail-props)
      (pretty-attributes/background-color-attributes thumbnail-props)
      (pretty-attributes/border-attributes           thumbnail-props)
      (pretty-attributes/clickable-state-attributes  thumbnail-props)
      (pretty-attributes/cursor-attributes           thumbnail-props)
      (pretty-attributes/flex-attributes             thumbnail-props)
      (pretty-attributes/full-block-size-attributes  thumbnail-props)
      (pretty-attributes/effect-attributes           thumbnail-props)
      (pretty-attributes/indent-attributes           thumbnail-props)
      (pretty-attributes/mouse-event-attributes      thumbnail-props)
      (pretty-attributes/react-attributes            thumbnail-props)
      (pretty-attributes/style-attributes            thumbnail-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn thumbnail-attributes
  ; @ignore
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ thumbnail-props]
  (-> {:class :pe-thumbnail}
      (pretty-attributes/class-attributes        thumbnail-props)
      (pretty-attributes/outdent-attributes      thumbnail-props)
      (pretty-attributes/state-attributes        thumbnail-props)
      (pretty-attributes/theme-attributes        thumbnail-props)
      (pretty-attributes/wrapper-size-attributes thumbnail-props)))
