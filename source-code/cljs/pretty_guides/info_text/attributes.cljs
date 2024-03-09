
(ns pretty-guides.info-text.attributes
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
  (-> {:class :pg-info-text--content}
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
  [id props]
  (-> {:class :pg-info-text--inner}
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
  (-> {:class :pg-info-text--outer}
      (pretty-models/container-model-outer-attributes props)
      (pretty-models/content-model-outer-attributes   props)))
