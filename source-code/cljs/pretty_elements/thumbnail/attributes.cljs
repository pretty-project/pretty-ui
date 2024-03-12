
(ns pretty-elements.thumbnail.attributes
    (:require [pretty-models.api :as pretty-models]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn canvas-attributes
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ props]
  (-> {:class :pe-thumbnail--canvas}
      (pretty-models/click-control-body-attributes  props)
      (pretty-models/flex-container-body-attributes props)
      (pretty-models/image-canvas-body-attributes   props)))

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
  (-> {:class :pe-thumbnail--inner}
      (pretty-models/click-control-inner-attributes  props)
      (pretty-models/flex-container-inner-attributes props)
      (pretty-models/image-canvas-inner-attributes   props)))

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
  (-> {:class :pe-thumbnail--outer}
      (pretty-models/click-control-outer-attributes  props)
      (pretty-models/flex-container-outer-attributes props)
      (pretty-models/image-canvas-outer-attributes   props)))
