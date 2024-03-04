
(ns pretty-diagrams.point-diagram.attributes
    (:require [pretty-attributes.api      :as pretty-attributes]
              [pretty-diagrams.engine.api :as pretty-diagrams.engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn datum-attributes
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; @param (integer) dex
  ; @param (*) datum
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [id props dex datum]
  (-> {:class :pd-point-diagram--datum}))

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
  (-> {:class :pd-point-diagram--inner}
      (pretty-attributes/inner-size-attributes  props)
      (pretty-attributes/inner-space-attributes props)
      (pretty-attributes/style-attributes       props)))

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
  (-> {:class :pd-point-diagram--outer}
      (pretty-attributes/class-attributes          props)
      (pretty-attributes/inner-position-attributes props)
      (pretty-attributes/outer-position-attributes props)
      (pretty-attributes/outer-size-attributes     props)
      (pretty-attributes/outer-space-attributes    props)
      (pretty-attributes/state-attributes          props)
      (pretty-attributes/theme-attributes          props)))
