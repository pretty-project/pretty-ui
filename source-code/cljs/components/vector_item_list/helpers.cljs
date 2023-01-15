
(ns components.vector-item-list.helpers
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn list-body-attributes
  ; @param (keyword) list-id
  ; @param (map) list-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [_ {:keys [style] :as list-props}]
  (-> {:style style}
      (pretty-css/indent-attributes list-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn list-attributes
  ; @param (keyword) list-id
  ; @param (map) list-props
  ;
  ; @return (map)
  [_ list-props]
  (-> {} (pretty-css/default-attributes list-props)
         (pretty-css/outdent-attributes list-props)))
