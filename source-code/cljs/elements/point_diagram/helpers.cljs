
(ns elements.point-diagram.helpers
    (:require [elements.element.helpers :as element.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [diagram-id {:keys [style] :as diagram-props}]
  (merge (element.helpers/element-indent-attributes diagram-id diagram-props)
         ; TEMP
         {:style (merge style {:width "500px" :height "300px"})}))

(defn diagram-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;
  ; @return (map)
  [diagram-id diagram-props]
  (merge (element.helpers/element-default-attributes diagram-id diagram-props)
         (element.helpers/element-outdent-attributes diagram-id diagram-props)))
