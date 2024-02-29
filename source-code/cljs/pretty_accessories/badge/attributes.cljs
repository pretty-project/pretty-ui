
(ns pretty-accessories.badge.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn badge-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) badge-id
  ; @param (map) badge-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ badge-props]
  (-> {:class :pa-badge--inner}
      (pretty-attributes/animation-attributes        badge-props)
      (pretty-attributes/background-color-attributes badge-props)
      (pretty-attributes/border-attributes           badge-props)
      (pretty-attributes/inner-size-attributes       badge-props)
      (pretty-attributes/inner-space-attributes      badge-props)
      (pretty-attributes/style-attributes            badge-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn badge-attributes
  ; @ignore
  ;
  ; @param (keyword) badge-id
  ; @param (map) badge-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ badge-props]
  (-> {:class :pa-badge}
      (pretty-attributes/class-attributes          badge-props)
      (pretty-attributes/inner-position-attributes badge-props)
      (pretty-attributes/outer-position-attributes badge-props)
      (pretty-attributes/outer-size-attributes     badge-props)
      (pretty-attributes/outer-space-attributes    badge-props)
      (pretty-attributes/state-attributes          badge-props)
      (pretty-attributes/theme-attributes          badge-props)))
