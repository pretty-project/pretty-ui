
(ns components.side-menu.helpers
    (:require [components.component.helpers :as component.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-body-attributes
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {:position (keyword)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:data-position (keyword)
  ;  :style (map)}
  [menu-id {:keys [position style] :as menu-props}]
  (merge (component.helpers/component-indent-attributes menu-id menu-props)
         {:data-position position
          :style         style}))

(defn menu-attributes
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ;
  ; @return (map)
  [menu-id menu-props]
  (merge (component.helpers/component-default-attributes menu-id menu-props)
         (component.helpers/component-outdent-attributes menu-id menu-props)))
