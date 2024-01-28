
(ns pretty-inputs.time-field.attributes
    (:require [pretty-css.layout.api :as pretty-css.layout]
              [pretty-css.basic.api :as pretty-css.basic]))

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
      (pretty-css.layout/indent-attributes field-props)
      (pretty-css.basic/style-attributes  field-props)))

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
      (pretty-css.basic/class-attributes   field-props)
      (pretty-css.layout/outdent-attributes field-props)
      (pretty-css.basic/state-attributes   field-props)))
