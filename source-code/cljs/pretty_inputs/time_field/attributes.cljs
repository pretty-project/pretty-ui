
(ns pretty-inputs.time-field.attributes
    (:require [pretty-css.api :as pretty-css]))

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
  (-> {:class :pi-time-field--body}
      (pretty-css/indent-attributes field-props)
      (pretty-css/style-attributes  field-props)))

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
      (pretty-css/class-attributes   field-props)
      (pretty-css/outdent-attributes field-props)
      (pretty-css/state-attributes   field-props)))
