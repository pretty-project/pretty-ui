
(ns pretty-inputs.multi-field.attributes
    (:require [pretty-attributes.api  :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn group-body-attributes
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ group-props]
  (-> {:class :pi-multi-field--body}
      (pretty-attributes/indent-attributes group-props)
      (pretty-attributes/style-attributes  group-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn group-attributes
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
       (-> {:class :pi-multi-field}
           (pretty-attributes/class-attributes  group-props)
           (pretty-attributes/outdent-attributes group-props)
           (pretty-attributes/state-attributes  group-props))))
