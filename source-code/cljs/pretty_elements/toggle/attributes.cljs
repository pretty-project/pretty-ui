
(ns pretty-elements.toggle.attributes
    (:require [pretty-models.api :as pretty-models]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn content-attributes
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ props]
  (-> {:class :pe-toggle--content}
      (pretty-models/clickable-model-content-attributes props)
      (pretty-models/container-model-content-attributes props)
      (pretty-models/content-model-content-attributes   props)))

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
  (-> {:class :pe-toggle--inner}
      (pretty-models/clickable-model-inner-attributes props)
      (pretty-models/container-model-inner-attributes props)
      (pretty-models/content-model-inner-attributes   props)))

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
  (-> {:class :pe-toggle--outer}
      (pretty-models/clickable-model-outer-attributes props)
      (pretty-models/container-model-outer-attributes props)
      (pretty-models/content-model-outer-attributes   props)))
