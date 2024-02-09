
(ns pretty-elements.horizontal-separator.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn separator-label-attributes
  ; @ignore
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ separator-props]
  (-> {:class :pe-horizontal-separator--label}
      (pretty-attributes/font-attributes separator-props)
      (pretty-attributes/text-attributes separator-props)))

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
  (-> {:class :pe-horizontal-separator--line}
      (pretty-attributes/line-attributes separator-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn separator-body-attributes
  ; @ignore
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ separator-props]
  (-> {:class :pe-horizontal-separator--body}
      (pretty-attributes/flex-attributes   separator-props)
      (pretty-attributes/indent-attributes separator-props)
      (pretty-attributes/size-attributes   separator-props)
      (pretty-attributes/style-attributes  separator-props)))

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
  (-> {:class :pe-horizontal-separator}
      (pretty-attributes/class-attributes        separator-props)
      (pretty-attributes/outdent-attributes      separator-props)
      (pretty-attributes/state-attributes        separator-props)
      (pretty-attributes/theme-attributes        separator-props)
      (pretty-attributes/wrapper-size-attributes separator-props)))
