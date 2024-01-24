
(ns pretty-inputs.multiline-field.prototypes
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-props-prototype
  ; @ignore
  ;
  ; @param (map) field-props
  ;
  ; @return (map)
  ; {:max-lines (integer)
  ;  :min-lines (integer)
  ;  :multiline? (boolean)}
  [field-props]
  (merge {:max-lines 32
          :min-lines 1}
         (-> field-props)
         {:multiline? true}))
