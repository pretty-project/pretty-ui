
(ns pretty-accessories.marker.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn marker-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) marker-id
  ; @param (map) marker-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [marker-id marker-props]
  (-> {:class :pa-marker--inner}
      (pretty-attributes/animation-attributes        marker-props)
      (pretty-attributes/background-color-attributes marker-props)
      (pretty-attributes/border-attributes           marker-props)
      (pretty-attributes/inner-size-attributes       marker-props)
      (pretty-attributes/inner-space-attributes      marker-props)
      (pretty-attributes/style-attributes            marker-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn marker-attributes
  ; @ignore
  ;
  ; @param (keyword) marker-id
  ; @param (map) marker-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ marker-props]
  (-> {:class :pa-marker}
      (pretty-attributes/class-attributes          marker-props)
      (pretty-attributes/inner-position-attributes marker-props)
      (pretty-attributes/outer-position-attributes marker-props)
      (pretty-attributes/outer-size-attributes     marker-props)
      (pretty-attributes/outer-space-attributes    marker-props)
      (pretty-attributes/state-attributes          marker-props)
      (pretty-attributes/theme-attributes          marker-props)))
