
(ns elements.blank.helpers
    (:require [elements.element.helpers :as element.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn blank-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) blank-id
  ; @param (map) blank-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [blank-id {:keys [style] :as blank-props}]
  (merge (element.helpers/element-indent-attributes blank-id blank-props)
         {:style style}))

(defn blank-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) blank-id
  ; @param (map) blank-props
  ;
  ; @return (map)
  [blank-id blank-props]
  (merge (element.helpers/element-default-attributes blank-id blank-props)
         (element.helpers/element-outdent-attributes blank-id blank-props)))
