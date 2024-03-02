
(ns pretty-inputs.password-field.prototypes
    (:require [fruits.vector.api                       :as vector]
              [pretty-inputs.password-field.adornments :as password-field.adornments]
              [pretty-inputs.password-field.env        :as password-field.env]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-props-prototype
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:end-adornments (maps in vector)(opt)}
  ;
  ; @return (map)
  ; {:end-adornments (maps in vector)
  ;  :label (multitype-content)
  ;  :placeholder (multitype-content)
  ;  :type (keyword)}
  [field-id {:keys [end-adornments] :as field-props}]
  (let [toggle-visibility-adornment (password-field.adornments/toggle-visibility-adornment field-id field-props)
        password-visible?           (password-field.env/password-visible?                  field-id field-props)]
       (merge {:label       :password
               :placeholder "••••••••"}
              (-> field-props)
              {:end-adornments (vector/conj-item end-adornments toggle-visibility-adornment)}
              {:type           (if password-visible? :text :password)})))
