
(ns pretty-accessories.badge.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn badge-icon-attributes
  ; @ignore
  ;
  ; @param (keyword) badge-id
  ; @param (map) badge-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [badge-id badge-props]
  (-> {:class :pa-badge--icon}
      (pretty-attributes/icon-attributes badge-props)))

(defn badge-label-attributes
  ; @ignore
  ;
  ; @param (keyword) badge-id
  ; @param (map) badge-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ badge-props]
  (-> {:class :pa-badge--label}
      (pretty-attributes/font-attributes badge-props)
      (pretty-attributes/text-attributes badge-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn badge-body-attributes
  ; @ignore
  ;
  ; @param (keyword) badge-id
  ; @param (map) badge-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [badge-id badge-props]
  (-> {:class :pa-badge--body}
      (pretty-attributes/background-color-attributes   badge-props)
      (pretty-attributes/border-attributes             badge-props)
      (pretty-attributes/quarter-block-size-attributes badge-props)
      (pretty-attributes/indent-attributes             badge-props)
      (pretty-attributes/style-attributes              badge-props)))

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
      (pretty-attributes/class-attributes        badge-props)
      (pretty-attributes/outdent-attributes      badge-props)
      (pretty-attributes/position-attributes     badge-props)
      (pretty-attributes/state-attributes        badge-props)
      (pretty-attributes/theme-attributes        badge-props)
      (pretty-attributes/wrapper-size-attributes badge-props)))
