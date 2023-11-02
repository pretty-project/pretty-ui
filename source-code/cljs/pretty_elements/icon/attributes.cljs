
(ns pretty-elements.icon.attributes
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn icon-body-attributes
  ; @ignore
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [icon-id {:keys [style] :as icon-props}]
  (-> {:class           :pe-icon--body
       :data-selectable false
       :style           style}
      (pretty-css/icon-attributes   icon-props)
      (pretty-css/indent-attributes icon-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn icon-attributes
  ; @ignore
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  ;
  ; @return (map)
  [_ icon-props]
  (-> {:class :pe-icon}
      (pretty-css/default-attributes icon-props)
      (pretty-css/outdent-attributes icon-props)))
