
(ns elements.stepper.helpers
    (:require [elements.element.helpers :as element.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn stepper-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) stepper-id
  ; @param (map) stepper-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [stepper-id {:keys [style] :as stepper-props}]
  (merge (element.helpers/element-indent-attributes stepper-id stepper-props)
         {:style style}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn stepper-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) stepper-id
  ; @param (map) stepper-props
  ;
  ; @return (map)
  [stepper-id stepper-props]
  (merge (element.helpers/element-default-attributes stepper-id stepper-props)
         (element.helpers/element-outdent-attributes stepper-id stepper-props)))
