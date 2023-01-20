
(ns elements.dropdown-menu.attributes
    (:require [elements.dropdown-menu.state :as dropdown-menu.state]
              [pretty-css.api               :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-content-attributes
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ menu-props]
  (-> {:class :e-dropdown-menu--content}
      (pretty-css/border-attributes menu-props)
      (pretty-css/color-attributes  menu-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-body-attributes
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)}
  [_ {:keys [style] :as menu-props}]
  (-> {:class                 :e-dropdown-menu--body
       :style                 style}
      (pretty-css/indent-attributes menu-props)))

(defn menu-attributes
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ;
  ; @return (map)
  ; {}
  [_ menu-props]
  (-> {:class        :e-dropdown-menu}
       ;:on-mouse-out #(reset! dropdown-menu.state/VISIBLE-CONTENT nil)}
      (pretty-css/default-attributes menu-props)
      (pretty-css/outdent-attributes menu-props)))
