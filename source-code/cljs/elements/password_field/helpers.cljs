
(ns elements.password-field.helpers
    (:require [elements.password-field.state :as password-field.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn password-visible?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (boolean)
  [field-id]
  (get @password-field.state/PASSWORD-VISIBILITY field-id))
