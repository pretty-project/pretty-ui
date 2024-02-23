
(ns pretty-elements.dropdown-menu.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

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
      (pretty-attributes/indent-attributes      menu-props)
      (pretty-attributes/inner-size-attributes  menu-props)
      (pretty-attributes/mouse-event-attributes menu-props)
      (pretty-attributes/style-attributes       menu-props)))

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
      (pretty-attributes/class-attributes      menu-props)
      (pretty-attributes/outdent-attributes    menu-props)
      (pretty-attributes/outer-size-attributes menu-props)
      (pretty-attributes/state-attributes      menu-props)
      (pretty-attributes/theme-attributes      menu-props)))
