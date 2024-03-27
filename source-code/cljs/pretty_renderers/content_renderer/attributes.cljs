
(ns pretty-renderers.content-renderer.attributes
    (:require [pretty-attributes.api :as pretty-attributes]
              [pretty-models.api :as pretty-models]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-attributes ;????
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; 
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ props]
  (-> {:class :pr-content-renderer--content}
      (pretty-models/flex-content-body-attributes    props)
      (pretty-models/plain-container-body-attributes props)))

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
  (-> {:class :pr-content-renderer--inner}
      (pretty-models/flex-content-inner-attributes    props)
      (pretty-models/plain-container-inner-attributes props)))

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
  (-> {:class :pr-content-renderer--outer}
      (pretty-models/flex-content-outer-attributes    props)
      (pretty-models/plain-container-outer-attributes props)))
