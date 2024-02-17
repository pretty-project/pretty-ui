
(ns pretty-accessories.marker.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn marker-body-attributes
  ; @ignore
  ;
  ; @param (keyword) marker-id
  ; @param (map) marker-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [marker-id marker-props]
  (-> {:class :pa-marker--body}
      (pretty-attributes/background-color-attributes marker-props)
      (pretty-attributes/indent-attributes           marker-props)
      (pretty-attributes/size-attributes             marker-props)
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
      (pretty-attributes/class-attributes        marker-props)
      (pretty-attributes/outdent-attributes      marker-props)
      (pretty-attributes/position-attributes     marker-props)
      (pretty-attributes/state-attributes        marker-props)
      (pretty-attributes/theme-attributes        marker-props)
      (pretty-attributes/wrapper-size-attributes marker-props)))
