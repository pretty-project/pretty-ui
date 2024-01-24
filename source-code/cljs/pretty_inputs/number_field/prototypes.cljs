
(ns pretty-inputs.number-field.prototypes
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-props-prototype
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ; {:type (keyword)}
  [_ field-props]
  (merge {}
         (-> field-props)
         {:type :number}))
