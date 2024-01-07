
(ns pretty-inputs.digit-field.utils
    (:require [pretty-inputs.digit-field.config :as digit-field.config]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-props->digits-width
  ; @ignore
  ;
  ; @param (map) field-props
  ;
  ; @return (integer)
  [field-props]
  (+ (* digit-field.config/DIGIT-WIDTH 4)
     (* digit-field.config/DIGIT-GAP   3)))
