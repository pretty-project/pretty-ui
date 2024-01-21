
(ns pretty-inputs.password-field.adornments
    (:require [pretty-inputs.password-field.env :as password-field.env]
              [pretty-inputs.password-field.side-effects :as password-field.side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn visibility-adornment
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  [field-id field-props]
  ; @bug (#9080)
  ; If the 'toggle-password-visibility!' function got the 'field-props' map, the field content
  ; started bouncing in high frequency instead of updating when any key got pressed.
  ; The state synchronizer
  (let [password-visible? (password-field.env/password-visible? field-id field-props)]
       {:icon-family     :material-symbols-filled
        :icon            (if password-visible? :visibility_off :visibility)
        :tooltip-content (if password-visible? :hide-password! :show-password!)
        :on-click-f      #(password-field.side-effects/toggle-password-visibility! field-id {})}))
