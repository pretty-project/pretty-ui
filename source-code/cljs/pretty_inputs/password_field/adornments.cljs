
(ns pretty-inputs.password-field.adornments
    (:require [pretty-inputs.password-field.env          :as password-field.env]
              [pretty-inputs.password-field.side-effects :as password-field.side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-visibility-adornment
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  [field-id field-props]
  (let [password-visible? (password-field.env/password-visible? field-id field-props)
        on-click-f        (fn [] (password-field.side-effects/toggle-password-visibility! field-id field-props))]
       {:icon-family     :material-symbols-filled
        :on-click-f      on-click-f
        :icon            (if password-visible? :visibility_off :visibility)
        :tooltip-content (if password-visible? :hide-password! :show-password!)}))
