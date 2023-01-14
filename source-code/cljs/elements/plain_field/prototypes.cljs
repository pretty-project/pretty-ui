
(ns elements.plain-field.prototypes
    (:require [candy.api              :refer [param return]]
              [elements.input.helpers :as input.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ; {:field-content-f (function)
  ;  :field-value-f (function)
  ;  :value-path (vector)}
  [field-id field-props]
  (merge {:field-content-f return
          :field-value-f   return
          :value-path      (input.helpers/default-value-path field-id)}
         (param field-props)))