
(ns pretty-elements.anchor.attributes
    (:require [pretty-models.api :as pretty-models]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-attributes
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ props]
  (-> {:class :pe-anchor--content}
      (pretty-models/click-control-body-attributes  props)
      (pretty-models/flex-container-body-attributes props)
      (pretty-models/plain-content-body-attributes  props)))

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
  (-> {:class :pe-anchor--inner}
      (pretty-models/click-control-inner-attributes  props)
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
  (-> {:class :pe-anchor--outer :style {:display :inline-block}}
      (pretty-models/click-control-outer-attributes  props)
      (pretty-models/flex-container-outer-attributes props)
      (pretty-models/plain-content-outer-attributes  props)))
