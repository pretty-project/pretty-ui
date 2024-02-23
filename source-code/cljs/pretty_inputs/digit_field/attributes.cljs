
(ns pretty-inputs.digit-field.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ field-props]
  (-> {:class :pi-digit-field--inner}
      (pretty-attributes/indent-attributes field-props)
      (pretty-attributes/style-attributes  field-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-attributes
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ; {}
  [_ field-props]
  (-> {:class :pi-digit-field}
      (pretty-attributes/class-attributes  field-props)
      (pretty-attributes/outdent-attributes field-props)
      (pretty-attributes/state-attributes  field-props)
      (pretty-attributes/theme-attributes   field-props)))
