
(ns elements.digit-field.helpers
    (:require [elements.digit-field.config :as digit-field.config]
              [elements.element.helpers    :as element.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-props->digits-width
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) field-props
  ;
  ; @return (integer)
  [field-props]
  (+ (* digit-field.config/DIGIT-WIDTH 4)
     (* digit-field.config/DIGIT-GAP   3)))

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

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
