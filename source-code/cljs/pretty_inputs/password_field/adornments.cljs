
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
  ; {}
  [field-id field-props]
  (let [on-click-f (fn [] (password-field.side-effects/toggle-password-visibility! field-id field-props))]
       (if (password-field.env/password-visible? field-id field-props)
           {:icon        :visibility_off
            :icon-family :material-symbols-filled
            :on-click-f  on-click-f}
           {:icon        :visibility
            :icon-family :material-symbols-filled
            :on-click-f  on-click-f})))
