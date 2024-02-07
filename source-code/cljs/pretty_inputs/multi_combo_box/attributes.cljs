
(ns pretty-inputs.multi-combo-box.attributes
    (:require [pretty-attributes.api        :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn box-body-attributes
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ box-props]
  (-> {:class :pi-multi-combo-box--body}
      (pretty-attributes/indent-attributes box-props)
      (pretty-attributes/style-attributes  box-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn box-attributes
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;
  ; @return (map)
  ; {}
  [_ box-props]
  (-> {:class :pi-multi-combo-box}
      (pretty-attributes/class-attributes  box-props)
      (pretty-attributes/outdent-attributes box-props)
      (pretty-attributes/state-attributes  box-props)
      (pretty-attributes/theme-attributes   box-props)))
