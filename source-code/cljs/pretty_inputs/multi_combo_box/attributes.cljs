
(ns pretty-inputs.multi-combo-box.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn inner-attributes
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ box-props]
  (-> {:class :pi-multi-combo-box--inner}
      (pretty-attributes/inner-space-attributes box-props)
      (pretty-attributes/style-attributes       box-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn outer-attributes
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;
  ; @return (map)
  ; {}
  [_ box-props]
  (-> {:class :pi-multi-combo-box--outer}
      (pretty-attributes/class-attributes  box-props)
      (pretty-attributes/outer-space-attributes box-props)
      (pretty-attributes/state-attributes  box-props)
      (pretty-attributes/theme-attributes   box-props)))
