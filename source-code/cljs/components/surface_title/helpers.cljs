
(ns components.surface-title.helpers
    (:require [components.component.helpers :as component.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn title-body-attributes
  ; @param (keyword) title-id
  ; @param (map) title-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [title-id {:keys [style] :as title-props}]
  (merge (component.helpers/component-outdent-attributes title-id title-props)
         {:style style}))

(defn title-attributes
  ; @param (keyword) title-id
  ; @param (map) title-props
  ;
  ; @return (map)
  [title-id title-props]
  (merge (component.helpers/component-default-attributes title-id title-props)
         (component.helpers/component-outdent-attributes title-id title-props)))
