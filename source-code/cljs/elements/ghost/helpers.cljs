
(ns elements.ghost.helpers
    (:require [elements.element.helpers :as element.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) ghost-id
  ; @param (map) ghost-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [ghost-id {:keys [border-radius height style] :as ghost-props}]
  (merge (element.helpers/element-indent-attributes ghost-id ghost-props)
         {:data-border-radius border-radius
          :data-height        height
          :style              style}))

(defn ghost-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) ghost-id
  ; @param (map) ghost-props
  ;
  ; @return (map)
  [ghost-id ghost-props]
  (merge (element.helpers/element-default-attributes ghost-id ghost-props)
         (element.helpers/element-outdent-attributes ghost-id ghost-props)))
