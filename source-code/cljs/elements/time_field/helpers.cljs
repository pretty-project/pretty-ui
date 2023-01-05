
(ns elements.time-field.helpers
    (:require [elements.element.helpers :as element.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [field-id {:keys [style] :as field-props}]
  (merge (element.helpers/element-indent-attributes field-id field-props)
         {:style style}))

(defn field-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  [field-id field-props]
  (merge (element.helpers/element-default-attributes field-id field-props)
         (element.helpers/element-outdent-attributes field-id field-props)))
