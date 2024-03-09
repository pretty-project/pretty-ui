
(ns pretty-diagrams.point-diagram.attributes
    (:require [pretty-models.api :as pretty-models]))

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
      (pretty-models/container-model-inner-attributes props)
      (pretty-models/shape-model-inner-attributes     props)))

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
      (pretty-models/container-model-outer-attributes props)
      (pretty-models/shape-model-outer-attributes     props)))
