
(ns pretty-layouts.sidebar.attributes
    (:require [pretty-models.api :as pretty-models]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn scroll-container-attributes
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ props]
  (-> {:class :pl-sidebar--scroll-container}
      (pretty-models/flex-container-content-attributes props)
      (pretty-models/plain-content-content-attributes  props)))

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
  (-> {:class :pl-sidebar--inner}
      (pretty-models/flex-container-inner-attributes props)
      (pretty-models/plain-content-inner-attributes  props)))

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
  (-> {:class :pl-sidebar--outer}
      (pretty-models/flex-container-outer-attributes props)
      (pretty-models/plain-content-outer-attributes  props)))
