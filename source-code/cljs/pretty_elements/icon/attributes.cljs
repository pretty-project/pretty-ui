
(ns pretty-elements.icon.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn icon-body-attributes
  ; @ignore
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ icon-props]
  (-> {:class :pe-icon--body}
      (pretty-attributes/icon-attributes   icon-props)
      (pretty-attributes/indent-attributes icon-props)
      (pretty-attributes/style-attributes  icon-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn icon-attributes
  ; @ignore
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ icon-props]
  (-> {:class :pe-icon}
      (pretty-attributes/class-attributes   icon-props)
      (pretty-attributes/outdent-attributes icon-props)
      (pretty-attributes/state-attributes   icon-props)
      (pretty-attributes/theme-attributes   icon-props)))
