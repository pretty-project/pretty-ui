
(ns pretty-inputs.digit-field.attributes
    (:require [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.basic.api      :as pretty-css.basic]
              [pretty-css.layout.api     :as pretty-css.layout]))

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
      (pretty-css.layout/indent-attributes field-props)
      (pretty-css.basic/style-attributes  field-props)))

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
      (pretty-css.basic/class-attributes   field-props)
      (pretty-css.layout/outdent-attributes field-props)
      (pretty-css.basic/state-attributes   field-props)
      (pretty-css.appearance/theme-attributes   field-props)))
