
(ns pretty-elements.icon.attributes
    (:require [pretty-css.api :as pretty-css]
              [pretty-css.content.api :as pretty-css.content]
              [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.layout.api :as pretty-css.layout]
              [pretty-css.basic.api :as pretty-css.basic]))

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
      (pretty-css.content/icon-attributes   icon-props)
      (pretty-css.layout/indent-attributes icon-props)
      (pretty-css.basic/style-attributes  icon-props)))

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
      (pretty-css.basic/class-attributes   icon-props)
      (pretty-css.layout/outdent-attributes icon-props)
      (pretty-css.basic/state-attributes   icon-props)
      (pretty-css.appearance/theme-attributes   icon-props)))
