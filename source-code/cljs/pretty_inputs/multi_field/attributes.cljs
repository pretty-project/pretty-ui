
(ns pretty-inputs.multi-field.attributes
    (:require [pretty-css.basic.api  :as pretty-css.basic]
              [pretty-css.layout.api :as pretty-css.layout]))

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
      (pretty-css.layout/indent-attributes group-props)
      (pretty-css.basic/style-attributes  group-props)))

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
           (pretty-css.basic/class-attributes   group-props)
           (pretty-css.layout/outdent-attributes group-props)
           (pretty-css.basic/state-attributes   group-props))))
