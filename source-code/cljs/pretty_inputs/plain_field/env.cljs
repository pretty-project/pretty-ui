
(ns pretty-inputs.plain-field.env
    (:require [pretty-inputs.core.env :as core.env]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-surface-visible?
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (boolean)
  [field-id _]
  (core.env/get-input-state field-id :surface-visible?))
