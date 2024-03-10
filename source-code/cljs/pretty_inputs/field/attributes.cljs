
(ns pretty-inputs.field.attributes
    (:require [pretty-models.api :as pretty-models]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-attributes
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ props]
  (-> {:class :pi-field--input}
      (pretty-models/flex-container-content-attributes    props)
      (pretty-models/field-input-content-attributes       props)
      (pretty-models/multiline-content-content-attributes props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn structure-attributes
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ props]
  (-> {:class :pi-field--structure}))

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
  (-> {:class :pi-field--inner}
      (pretty-models/flex-container-inner-attributes    props)
      (pretty-models/field-input-inner-attributes       props)
      (pretty-models/multiline-content-inner-attributes props)))

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
  (-> {:class :pi-field--outer}
      (pretty-models/flex-container-outer-attributes    props)
      (pretty-models/field-input-outer-attributes       props)
      (pretty-models/multiline-content-outer-attributes props)))
