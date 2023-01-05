
(ns components.side-menu.helpers
    (:require [components.component.helpers :as component.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-body-attributes
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [_ {:keys [style]}]
  {:style style})

(defn menu-attributes
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ;
  ; @return (map)
  [menu-id menu-props]
  (merge (component.helpers/component-default-attributes menu-id menu-props)
         (component.helpers/component-indent-attributes  menu-id menu-props)))
