
(ns elements.password-field.prototypes
    (:require [elements.password-field.env :as password-field.env]
              [elements.plain-field.env    :as plain-field.env]
              [noop.api                    :refer [param]]
              [vector.api                  :as vector]))

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
        :on-click        {:fx [:elements.password-field/toggle-password-visibility! field-id]}}))

(defn field-props-prototype
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:end-adornments (maps in vector)(opt)
  ;  :validate? (boolean)}
  ;
  ; @return (map)
  ; {:helper (metamorphic-content)
  ;  :label (metamorphic-content)
  ;  :type (keyword)}
  [field-id {:keys [end-adornments] :as field-props}]
  (let [field-empty?      (plain-field.env/field-empty?         field-id)
        password-visible? (password-field.env/password-visible? field-id)]
       (merge {:label :password
               :type  (if password-visible? :text :password)}
              (param field-props)

              ; ...
              (let [visibility-adornment (visibility-adornment-prototype field-id field-props)]
                   {:end-adornments (vector/conj-item end-adornments visibility-adornment)}))))
