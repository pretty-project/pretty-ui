
(ns elements.ghost.helpers
    (:require [elements.element.helpers :as element.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) ghost-id
  ; @param (map) ghost-props
  ; {:border-radius (keyword)(opt)
  ;  :height (keyword)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:data-border-radius (keyword)
  ;  :data-block-height (keyword)
  ;  :style (map)}
  [ghost-id {:keys [border-radius height style] :as ghost-props}]
  (merge (element.helpers/element-indent-attributes ghost-id ghost-props)
         {:data-border-radius border-radius
          :data-block-height  height
          :style              style}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
