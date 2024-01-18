
(ns pretty-inputs.multiline-field.prototypes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-props-prototype
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ; {:max-lines (integer)
  ;  :min-lines (integer)
  ;  :multiline? (boolean)}
  [field-id field-props]
  (merge {:max-lines 32
          :min-lines 1}
         (-> field-props)
         {:multiline? true}))
