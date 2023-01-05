
(ns elements.password-field.prototypes
    (:require [candy.api                       :refer [param]]
              [elements.password-field.helpers :as password-field.helpers]
              [elements.text-field.helpers     :as text-field.helpers]
              [vector.api                      :as vector]
              [x.user.api                      :as x.user]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
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
  (let [field-empty?      (text-field.helpers/field-empty?          field-id)
        password-visible? (password-field.helpers/password-visible? field-id)]
       (merge {:label :password
               :type  (if password-visible? :text :password)}
              (param field-props)
              ; *
              (if validate? {:helper    {:content :valid-password-rules :replacements ["8"]}
                             :validator {:f               x.user/password?
                                         :invalid-message :password-is-too-weak}})
              ; *
              (let [show-password-adornment {;:disabled? field-empty?
                                             :icon      (if password-visible? :visibility_off :visibility)
                                             :tooltip   (if password-visible? :hide-password! :show-password!)
                                             :on-click  {:fx [:elements.password-field/toggle-password-visibility! field-id]}}]
                   {:end-adornments (vector/conj-item end-adornments show-password-adornment)}))))
