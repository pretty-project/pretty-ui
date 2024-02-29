
(ns pretty-accessories.overlay.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn overlay-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) overlay-id
  ; @param (map) overlay-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ overlay-props]
  (-> {:class :pa-overlay--inner}
      (pretty-attributes/animation-attributes        overlay-props)
      (pretty-attributes/background-color-attributes overlay-props)
      (pretty-attributes/inner-size-attributes       overlay-props)
      (pretty-attributes/inner-space-attributes      overlay-props)
      (pretty-attributes/mouse-event-attributes      overlay-props)
      (pretty-attributes/style-attributes            overlay-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn overlay-attributes
  ; @ignore
  ;
  ; @param (keyword) overlay-id
  ; @param (map) overlay-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ overlay-props]
  (-> {:class :pa-overlay}
      (pretty-attributes/class-attributes          overlay-props)
      (pretty-attributes/inner-position-attributes overlay-props)
      (pretty-attributes/outer-position-attributes overlay-props)
      (pretty-attributes/outer-size-attributes     overlay-props)
      (pretty-attributes/outer-space-attributes    overlay-props)
      (pretty-attributes/state-attributes          overlay-props)
      (pretty-attributes/theme-attributes          overlay-props)
      (pretty-attributes/visibility-attributes     overlay-props)))
