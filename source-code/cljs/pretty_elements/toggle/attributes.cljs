
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
      (pretty-models/click-control-content-attributes   props)
      (pretty-models/flex-content-content-attributes    props)
      (pretty-models/plain-container-content-attributes props)))

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
      (pretty-models/click-control-inner-attributes   props)
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
  (-> {:class :pe-toggle--outer}
      (pretty-models/click-control-outer-attributes   props)
      (pretty-models/flex-content-outer-attributes    props)
      (pretty-models/plain-container-outer-attributes props)))
