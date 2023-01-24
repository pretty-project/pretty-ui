
(ns elements.dropdown-menu.attributes
    (:require [elements.dropdown-menu.state :as dropdown-menu.state]
              [pretty-css.api               :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-surface-body-attributes
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ {:keys [surface]}]
  (-> {:class :e-dropdown-menu--surface-body}
      (pretty-css/border-attributes surface)
      (pretty-css/color-attributes  surface)
      (pretty-css/indent-attributes surface)))

(defn menu-surface-attributes
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ {:keys [surface]}]
  (-> {:class :e-dropdown-menu--surface}
      (pretty-css/outdent-attributes surface)))

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
  (-> {:class :e-dropdown-menu--body
       :style style}
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
