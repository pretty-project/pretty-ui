
(ns pretty-inputs.time-field.attributes
    (:require [pretty-attributes.basic.api  :as pretty-attributes.basic]
              [pretty-attributes.layout.api :as pretty-attributes.layout]))

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
  (-> {:class :pi-time-field--inner}
      (pretty-attributes/inner-space-attributes field-props)
      (pretty-attributes/style-attributes  field-props)))

(defn field-attributes
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ; {}
  [_ field-props]
  (-> {:class :pi-time-field}
      (pretty-attributes/class-attributes  field-props)
      (pretty-attributes/outer-space-attributes field-props)
      (pretty-attributes/state-attributes  field-props)))
