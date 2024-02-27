
(ns pretty-inputs.value.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn value-content-attributes
  ; @ignore
  ;
  ; @param (keyword) value-id
  ; @param (map) value-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ value-props]
  (-> {:class :pi-value--content}
      (pretty-attributes/font-attributes value-props)
      (pretty-attributes/text-attributes value-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn value-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) value-id
  ; @param (map) value-props
  ; {}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ value-props]
  (-> {:class :pi-value--inner}
      (pretty-attributes/background-color-attributes value-props)
      (pretty-attributes/border-attributes           value-props)
      (pretty-attributes/flex-attributes             value-props)
      (pretty-attributes/inner-size-attributes       value-props)
      (pretty-attributes/inner-space-attributes      value-props)
      (pretty-attributes/style-attributes            value-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn value-attributes
  ; @ignore
  ;
  ; @param (keyword) value-id
  ; @param (map) value-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ value-props]
  (-> {:class :pi-value}
      (pretty-attributes/class-attributes          value-props)
      (pretty-attributes/inner-position-attributes value-props)
      (pretty-attributes/outer-position-attributes value-props)
      (pretty-attributes/outer-size-attributes     value-props)
      (pretty-attributes/outer-space-attributes    value-props)
      (pretty-attributes/state-attributes          value-props)
      (pretty-attributes/theme-attributes          value-props)))
