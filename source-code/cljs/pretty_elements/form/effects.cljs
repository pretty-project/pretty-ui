
(ns pretty-elements.form.effects
    (:require [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-elements.form/validate-input!
  ; @param (keyword) input-id
  ; @param (keyword) validation-props
  ; {:on-invalid (Re-Frame metamorphic-event)(opt)
  ;   This event takes the input value and the invalid message as its last parameter.
  ;  :on-valid (Re-Frame metamorphic-event)(opt)
  ;   This event takes the input value as its last parameter.}
  ;
  ; @usage
  ; [:pretty-elements.form/validate-input! :my-input {...}]
  (fn [_ [_ input-id validation-props]]
      {:fx [:pretty-elements.form/validate-input! input-id validation-props]}))

(r/reg-event-fx :pretty-elements.form/validate-form!
  ; @param (keyword) form-id
  ; @param (keyword) validation-props
  ; {:on-invalid (Re-Frame metamorphic-event)(opt)
  ;   This event takes the invalid message as its last parameter.
  ;  :on-valid (Re-Frame metamorphic-event)(opt)}
  ;
  ; @usage
  ; [:pretty-elements.form/validate-form! :my-form {...}]
  (fn [_ [_ form-id validation-props]]
      {:fx [:pretty-elements.form/validate-form! form-id validation-props]}))
