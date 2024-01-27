
(ns pretty-elements.icon.attributes
    (:require [pretty-css.api :as pretty-css]
              [pretty-css.layout.api :as pretty-css.layout]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn icon-body-attributes
  ; @ignore
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  ;
  ; @return (map)
  ; {}
  [icon-id icon-props]
  (-> {:class :pe-icon--body}
      (pretty-css/icon-attributes   icon-props)
      (pretty-css.layout/indent-attributes icon-props)
      (pretty-css/style-attributes  icon-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn icon-attributes
  ; @ignore
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  ;
  ; @return (map)
  ; {}
  [_ icon-props]
  (-> {:class :pe-icon}
      (pretty-css/class-attributes   icon-props)
      (pretty-css.layout/outdent-attributes icon-props)
      (pretty-css/state-attributes   icon-props)
      (pretty-css/theme-attributes   icon-props)))
