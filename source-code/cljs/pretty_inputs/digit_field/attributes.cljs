
(ns pretty-inputs.digit-field.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-body-attributes
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ field-props]
  (-> {:class :pi-digit-field--body}
      (pretty-build-kit/indent-attributes field-props)
      (pretty-build-kit/style-attributes  field-props)))

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
      (pretty-build-kit/class-attributes   field-props)
      (pretty-build-kit/outdent-attributes field-props)
      (pretty-build-kit/state-attributes   field-props)))
