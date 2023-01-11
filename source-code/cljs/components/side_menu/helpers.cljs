
(ns components.side-menu.helpers
    (:require [components.component.helpers :as component.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-body-attributes
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {:min-width (keyword)(opt)
  ;  :position (keyword)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:data-element-min-width (keyword)
  ;  :data-position (keyword)
  ;  :style (map)}
  [menu-id {:keys [min-width position style] :as menu-props}]
  (merge (component.helpers/component-indent-attributes menu-id menu-props)
         {:data-element-min-width min-width
          :data-position          position
          :style                  style}))

(defn menu-attributes
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ;
  ; @return (map)
  [menu-id menu-props]
  (merge (component.helpers/component-default-attributes menu-id menu-props)
         (component.helpers/component-outdent-attributes menu-id menu-props)))
