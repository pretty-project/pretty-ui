
(ns pretty-elements.menu-bar.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-bar-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ bar-props]
  (-> {:class :pe-menu-bar--inner}
      (pretty-attributes/background-color-attributes bar-props)
      (pretty-attributes/border-attributes           bar-props)
      (pretty-attributes/flex-attributes             bar-props)
      (pretty-attributes/inner-size-attributes       bar-props)
      (pretty-attributes/inner-space-attributes      bar-props)
      (pretty-attributes/style-attributes            bar-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-bar-attributes
  ; @ignore
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ bar-props]
  (-> {:class :pe-menu-bar}
      (pretty-attributes/class-attributes          bar-props)
      (pretty-attributes/inner-position-attributes bar-props)
      (pretty-attributes/outer-position-attributes bar-props)
      (pretty-attributes/outer-size-attributes     bar-props)
      (pretty-attributes/outer-space-attributes    bar-props)
      (pretty-attributes/state-attributes          bar-props)
      (pretty-attributes/theme-attributes          bar-props)))
