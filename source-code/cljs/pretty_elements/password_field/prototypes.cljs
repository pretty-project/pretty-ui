
(ns pretty-elements.password-field.prototypes
    (:require [pretty-elements.password-field.env :as password-field.env]
              [pretty-elements.plain-field.env    :as plain-field.env]
              [vector.api                         :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn visibility-adornment-prototype
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ; {}
  [field-id _]
  (let [password-visible? (password-field.env/password-visible? field-id)]
       {:icon-family     :material-symbols-filled
        :icon            (if password-visible? :visibility_off :visibility)
        :tooltip-content (if password-visible? :hide-password! :show-password!)
        :on-click        {:fx [:pretty-elements.password-field/toggle-password-visibility! field-id]}}))

(defn field-props-prototype
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:end-adornments (maps in vector)(opt)}
  ;
  ; @return (map)
  ; {:end-adornments (maps in vector)
  ;  :label (metamorphic-content)
  ;  :placeholder (metamorphic-content)
  ;  :type (keyword)}
  [field-id {:keys [end-adornments] :as field-props}]
  (let [field-empty?      (plain-field.env/field-empty?         field-id)
        password-visible? (password-field.env/password-visible? field-id)]
       (merge {:label       :password
               :placeholder "••••••••"}
              (-> field-props)
              {:type (if password-visible? :text :password)}

              ; ...
              (let [visibility-adornment (visibility-adornment-prototype field-id field-props)]
                   {:end-adornments (vector/conj-item end-adornments visibility-adornment)}))))
