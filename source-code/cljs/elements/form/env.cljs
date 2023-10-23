
(ns elements.form.env
    (:require [elements.form.state :as form.state]
              [re-frame.api        :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-input-stored-value
  ; @ignore
  ;
  ; @description
  ; Returns the stored value of the form input registered with the given input ID.
  ;
  ; @param (keyword) input-id
  ;
  ; @return (*)
  [input-id]
  (let [value-path (get-in @form.state/FORM-INPUTS [input-id :value-path])
        get-value-f (get-in @form.state/FORM-INPUTS [input-id :get-value-f])]
       (-> [:get-item value-path] r/subscribe deref)
       (get-value-f)))

(defn get-input-validators
  ; @ignore
  ;
  ; @description
  ; Returns the validators of the form input registered with the given input ID.
  ;
  ; @param (keyword) input-id
  ;
  ; @return (maps in vector)
  [input-id]
  (get-in @form.state/FORM-INPUTS [input-id :validators]))
