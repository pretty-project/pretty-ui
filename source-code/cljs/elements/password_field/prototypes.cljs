
(ns elements.password-field.prototypes
    (:require [elements.password-field.helpers :as password-field.helpers]
              [elements.plain-field.helpers    :as plain-field.helpers]
              [noop.api                        :refer [param]]
              [vector.api                      :as vector]
              [x.user.api                      :as x.user]))

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
  (let [password-visible? (password-field.helpers/password-visible? field-id)]
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
  ;  :type (keyword)
  ;  :validator (map)
  ;   {:f (function)
  ;    :invalid-message (keyword)}}
  [field-id {:keys [end-adornments validate?] :as field-props}]
  (let [field-empty?      (plain-field.helpers/field-empty?         field-id)
        password-visible? (password-field.helpers/password-visible? field-id)]
       (merge {:label :password
               :type  (if password-visible? :text :password)}
              (param field-props)

              ; ...
              (if validate? {:helper    {:content :valid-password-rules :replacements ["8"]}
                             :validator {:f x.user/password?
                                         :invalid-message :password-is-too-weak}})

              ; ...
              (let [visibility-adornment (visibility-adornment-prototype field-id field-props)]
                   {:end-adornments (vector/conj-item end-adornments visibility-adornment)}))))
