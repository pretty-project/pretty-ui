
(ns pretty-layouts.surface.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-content-attributes
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ surface-props]
  (-> {:class :pl-surface--content}
      (pretty-attributes/content-size-attributes surface-props)
      (pretty-attributes/overflow-attributes     surface-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ surface-props]
  (-> {:class :pl-surface--inner}
      (pretty-attributes/animation-attributes        surface-props)
      (pretty-attributes/background-color-attributes surface-props)
      (pretty-attributes/flex-attributes             surface-props)
      (pretty-attributes/inner-size-attributes       surface-props)
      (pretty-attributes/inner-space-attributes      surface-props)
      (pretty-attributes/style-attributes            surface-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-attributes
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ surface-props]
  (-> {:class :pl-surface}
      (pretty-attributes/class-attributes          surface-props)
      (pretty-attributes/inner-position-attributes surface-props)
      (pretty-attributes/outer-position-attributes surface-props)
      (pretty-attributes/outer-size-attributes     surface-props)
      (pretty-attributes/outer-space-attributes    surface-props)
      (pretty-attributes/state-attributes          surface-props)
      (pretty-attributes/theme-attributes          surface-props)))
