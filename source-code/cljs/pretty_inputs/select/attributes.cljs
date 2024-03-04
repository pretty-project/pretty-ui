
(ns pretty-inputs.select.attributes
    (:require [dom.api                  :as dom]
              [pretty-attributes.api    :as pretty-attributes]
              [pretty-inputs.engine.api :as pretty-inputs.engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn inner-attributes
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;
  ; @return (map)
  ; {}
  [_ select-props]
  (-> {:class :pi-select--inner}
      (pretty-attributes/inner-space-attributes select-props)
      (pretty-attributes/style-attributes  select-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn outer-attributes
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;
  ; @return (map)
  ; {}
  [_ select-props]
  (-> {:class :pi-select--outer}
      (pretty-attributes/class-attributes  select-props)
      (pretty-attributes/outer-space-attributes select-props)
      (pretty-attributes/state-attributes  select-props)
      (pretty-attributes/theme-attributes   select-props)))
