
(ns components.side-menu.helpers
    (:require [components.component.helpers :as component.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-body-attributes
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {:min-width (keyword)(opt)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:data-element-min-width (keyword)
  ;  :style (map)}
  [menu-id {:keys [min-width style] :as menu-props}]
  (merge (component.helpers/component-indent-attributes menu-id menu-props)
         {:data-element-min-width min-width
          :style                  style}))

(defn menu-attributes
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {:position (keyword)}
  ;
  ; @return (map)
  ; {:data-position (keyword)}
  [menu-id {:keys [position] :as menu-props}]
  (merge (component.helpers/component-default-attributes menu-id menu-props)
         (component.helpers/component-outdent-attributes menu-id menu-props)
         {:data-position position}))
