
(ns pretty-elements.dropdown-menu.attributes
    (:require [pretty-css.appearance.api           :as pretty-css.appearance]
              [pretty-css.basic.api                :as pretty-css.basic]
              [pretty-css.layout.api               :as pretty-css.layout]
              [pretty-css.control.api               :as pretty-css.control]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-body-attributes
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ menu-props]
  (-> {:class :pe-dropdown-menu--body}
      (pretty-css.basic/style-attributes         menu-props)
      (pretty-css.control/mouse-event-attributes menu-props)
      (pretty-css.layout/indent-attributes       menu-props)))

(defn menu-attributes
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ menu-props]
  (-> {:class :pe-dropdown-menu}
      (pretty-css.appearance/theme-attributes menu-props)
      (pretty-css.basic/class-attributes      menu-props)
      (pretty-css.layout/outdent-attributes   menu-props)
      (pretty-css.basic/state-attributes      menu-props)))
