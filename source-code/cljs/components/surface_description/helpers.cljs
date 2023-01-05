
(ns components.surface-description.helpers
    (:require [components.component.helpers :as component.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn description-body-attributes
  ; @param (keyword) description-id
  ; @param (map) description-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [description-id {:keys [style] :as description-props}]
  (merge (component.helpers/component-indent-attributes description-id description-props)
         {:style style}))

(defn description-attributes
  ; @param (keyword) description-id
  ; @param (map) description-props
  ;
  ; @return (map)
  [description-id description-props]
  (merge (component.helpers/component-default-attributes description-id description-props)
         (component.helpers/component-outdent-attributes description-id description-props)))
