
(ns pretty-website.multi-menu.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ menu-props]
  (-> {:class :pw-multi-menu--inner}
      (pretty-attributes/indent-attributes menu-props)
      (pretty-attributes/style-attributes  menu-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
  (-> {:class :pw-multi-menu}
      (pretty-attributes/class-attributes  menu-props)
      (pretty-attributes/state-attributes  menu-props)
      (pretty-attributes/outdent-attributes menu-props)
      (pretty-attributes/theme-attributes   menu-props)))
