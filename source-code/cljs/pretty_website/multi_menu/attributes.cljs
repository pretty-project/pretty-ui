
(ns pretty-website.multi-menu.attributes
    (:require [pretty-css.api :as pretty-css]
              [pretty-css.layout.api :as pretty-css.layout]))

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
      (pretty-css.layout/indent-attributes menu-props)
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
      (pretty-css.layout/outdent-attributes menu-props)
      (pretty-css/theme-attributes   menu-props)))
