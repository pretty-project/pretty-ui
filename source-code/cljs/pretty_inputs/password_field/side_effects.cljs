
(ns pretty-inputs.password-field.side-effects
    (:require [pretty-inputs.engine.api :as pretty-inputs.engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-password-visibility!
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id _]
  ; Resets the password visibility.
  ; When a password field unmounts it's important to reset its visibility state;
  ; otherwise, newly mounted password fields would remember their previous instance's state,
  ; and it's strongly not recommended to mount a password field with a visible content!
  (pretty-inputs.engine/update-input-state! field-id dissoc :password-visible?))

(defn toggle-password-visibility!
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id _]
  (pretty-inputs.engine/update-input-state! field-id update :password-visible? not))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-will-unmount
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  (reset-password-visibility! field-id field-props))
