
(ns elements.multi-field.attributes
    (:require [pretty-css.api   :as pretty-css]))

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
  (-> {:class :e-multi-field--body
       :style style}
      (pretty-css/indent-attributes group-props)))

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
  ; Each field separatelly reacts to the disabled state, therefore no need
  ; to the group reacts to it.
  (let [group-props (dissoc group-props :disabled?)]
       (-> {:class :e-multi-field}
           (pretty-css/default-attributes group-props)
           (pretty-css/outdent-attributes group-props))))
