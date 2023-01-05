
(ns elements.chip-group.helpers
    (:require [elements.element.helpers :as element.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-group-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [group-id {:keys [style] :as group-props}]
  (merge (element.helpers/element-indent-attributes group-id group-props)
         {:style style}))

(defn chip-group-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  [group-id group-props]
  (merge (element.helpers/element-default-attributes group-id group-props)
         (element.helpers/element-outdent-attributes group-id group-props)))
