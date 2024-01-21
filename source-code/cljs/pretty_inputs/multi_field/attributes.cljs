
(ns pretty-inputs.multi-field.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn group-body-attributes
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ group-props]
  (-> {:class :pi-multi-field--body}
      (pretty-build-kit/indent-attributes group-props)
      (pretty-build-kit/style-attributes  group-props)))

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
           (pretty-build-kit/class-attributes   group-props)
           (pretty-build-kit/outdent-attributes group-props)
           (pretty-build-kit/state-attributes   group-props))))
