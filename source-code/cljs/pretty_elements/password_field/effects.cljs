
(ns pretty-elements.password-field.effects
    (:require [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-f :pretty-elements.password-field/field-will-unmount
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  (fn [_ [_ field-id _]]
      {:fx [:pretty-elements.password-field/reset-password-visibility! field-id]}))
