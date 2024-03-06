
(ns pretty-inputs.multi-field.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn inner-attributes
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ group-props]
  (-> {:class :pi-multi-field--inner}
      (pretty-attributes/inner-space-attributes group-props)
      (pretty-attributes/style-attributes       group-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn outer-attributes
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  ; {}
  [_ group-props]
  ; Each field separatelly reacts to the disabled state.
  ; Therefore, no need to the group reacts to it.
  (let [group-props (dissoc group-props :disabled?)]
       (-> {:class :pi-multi-field--outer}
           (pretty-attributes/class-attributes  group-props)
           (pretty-attributes/outer-space-attributes group-props)
           (pretty-attributes/state-attributes  group-props))))
