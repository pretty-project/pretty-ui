
(ns pretty-accessories.label.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-content-attributes
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ label-props]
  (-> {:class :pa-label--content}
      (pretty-attributes/font-attributes label-props)
      (pretty-attributes/text-attributes label-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ label-props]
  (-> {:class :pa-label--inner}
      (pretty-attributes/background-color-attributes label-props)
      (pretty-attributes/border-attributes           label-props)
      (pretty-attributes/flex-attributes             label-props)
      (pretty-attributes/inner-size-attributes       label-props)
      (pretty-attributes/inner-space-attributes      label-props)
      (pretty-attributes/style-attributes            label-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-attributes
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ label-props]
  (-> {:class :pa-label}
      (pretty-attributes/class-attributes          label-props)
      (pretty-attributes/inner-position-attributes label-props)
      (pretty-attributes/outer-position-attributes label-props)
      (pretty-attributes/outer-size-attributes     label-props)
      (pretty-attributes/outer-space-attributes    label-props)
      (pretty-attributes/state-attributes          label-props)
      (pretty-attributes/theme-attributes          label-props)))