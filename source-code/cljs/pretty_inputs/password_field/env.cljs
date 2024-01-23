
(ns pretty-inputs.password-field.env
    (:require [pretty-engine.api :as pretty-engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn password-visible?
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (boolean)
  [field-id _]
  (pretty-engine/get-input-state field-id :password-visible?))
