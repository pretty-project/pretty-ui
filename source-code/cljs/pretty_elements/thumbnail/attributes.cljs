
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
      (pretty-models/clickable-model-content-attributes props)
      (pretty-models/container-model-content-attributes props)
      (pretty-models/image-model-content-attributes     props)))

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
      (pretty-models/clickable-model-inner-attributes props)
      (pretty-models/container-model-inner-attributes props)
      (pretty-models/image-model-inner-attributes     props)))

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
      (pretty-models/clickable-model-outer-attributes props)
      (pretty-models/container-model-outer-attributes props)
      (pretty-models/image-model-outer-attributes     props)))
