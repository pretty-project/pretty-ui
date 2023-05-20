
(ns elements.password-field.side-effects
    (:require [elements.password-field.state :as password-field.state]
              [re-frame.api                  :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-password-visibility!
  ; @ignore
  ;
  ; @description
  ; Resets the password visibility state. When a password field unmounts it's
  ; important to reset its visibility state, otherwise the newly mounted password
  ; fields could remember their previous instance's state and it's highly not
  ; recommended to mount a password field with a visibility ON state!
  ;
  ; @param (keyword) field-id
  [field-id]
  (swap! password-field.state/PASSWORD-VISIBILITY dissoc field-id))

(defn toggle-password-visibility!
  ; @ignore
  ;
  ; @param (keyword) field-id
  [field-id]
  (swap! password-field.state/PASSWORD-VISIBILITY update field-id not))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @param (keyword) field-id
(r/reg-fx :elements.password-field/reset-password-visibility! reset-password-visibility!)

; @ignore
;
; @param (keyword) field-id
(r/reg-fx :elements.password-field/toggle-password-visibility! toggle-password-visibility!)
