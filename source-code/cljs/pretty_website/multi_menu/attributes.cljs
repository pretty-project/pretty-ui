
(ns pretty-website.multi-menu.attributes
    (:require [pretty-css.api :as pretty-css]
              [pretty-css.layout.api :as pretty-css.layout]
              [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.basic.api :as pretty-css.basic]))

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
      (pretty-css.basic/style-attributes  menu-props)))

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
      (pretty-css.basic/class-attributes   menu-props)
      (pretty-css.basic/state-attributes   menu-props)
      (pretty-css.layout/outdent-attributes menu-props)
      (pretty-css.appearance/theme-attributes   menu-props)))
