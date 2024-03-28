
(ns pretty-inputs.password-field.adornments
    (:require [component-props.api :as component-props]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-visibility-adornment
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  ; {:icon (map)
  ;  :on-click-f (function)}
  [id _]
  (let [on-click-f (fn [_] (component-props/update-props! id update :password-visible? not))]
       (if (component-props/get-prop id :password-visible?)
           {:icon {:icon-name :visibility_off :icon-family :material-symbols-filled} :on-click-f on-click-f}
           {:icon {:icon-name :visibility     :icon-family :material-symbols-filled} :on-click-f on-click-f})))
