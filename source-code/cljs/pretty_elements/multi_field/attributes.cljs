
(ns pretty-elements.multi-field.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn group-body-attributes
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)}
  [_ {:keys [style] :as group-props}]
  (-> {:class :pe-multi-field--body
       :style style}
      (pretty-build-kit/indent-attributes group-props)))

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
       (-> {:class :pe-multi-field}
           (pretty-build-kit/class-attributes   group-props)
           (pretty-build-kit/outdent-attributes group-props)
           (pretty-build-kit/state-attributes   group-props))))
