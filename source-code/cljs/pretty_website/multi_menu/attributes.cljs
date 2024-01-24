
(ns pretty-website.multi-menu.attributes
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-body-attributes
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ menu-props]
  (-> {:class :pw-multi-menu--body}
      (pretty-css/indent-attributes menu-props)
      (pretty-css/style-attributes  menu-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-attributes
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ menu-props]
  (-> {:class :pw-multi-menu}
      (pretty-css/class-attributes   menu-props)
      (pretty-css/state-attributes   menu-props)
      (pretty-css/outdent-attributes menu-props)))
