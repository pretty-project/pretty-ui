
(ns pretty-elements.vertical-separator.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn separator-line-attributes
  ; @ignore
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ separator-props]
  (-> {:class :pe-vertical-separator--line}
      (pretty-attributes/line-attributes separator-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn separator-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ separator-props]
  (-> {:class :pe-vertical-separator--inner}
      (pretty-attributes/flex-attributes        separator-props)
      (pretty-attributes/inner-size-attributes  separator-props)
      (pretty-attributes/inner-space-attributes separator-props)
      (pretty-attributes/style-attributes       separator-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn separator-attributes
  ; @ignore
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ separator-props]
  (-> {:class :pe-vertical-separator}
      (pretty-attributes/class-attributes          separator-props)
      (pretty-attributes/inner-position-attributes separator-props)
      (pretty-attributes/outer-position-attributes separator-props)
      (pretty-attributes/outer-size-attributes     separator-props)
      (pretty-attributes/outer-space-attributes    separator-props)
      (pretty-attributes/state-attributes          separator-props)
      (pretty-attributes/theme-attributes          separator-props)))
