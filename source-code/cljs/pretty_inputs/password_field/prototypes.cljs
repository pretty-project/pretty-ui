
(ns pretty-inputs.password-field.prototypes
    (:require [dynamic-props.api                       :as dynamic-props]
              [form-validator.api                      :as form-validator]
              [fruits.map.api                          :as map]
              [fruits.vector.api                       :as vector]
              [pretty-inputs.password-field.adornments :as password-field.adornments]
              [pretty-models.api                       :as pretty-models]
              [pretty-properties.api                   :as pretty-properties]
              [pretty-subitems.api                     :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; @param (map) field
  ;
  ; @return (map)
  [id props field]
  (let [toggle-visibility-adornment (password-field.adornments/toggle-visibility-adornment id props)
        password-visible?           (dynamic-props/get-prop id :password-visible?)]
       (-> field (pretty-properties/default-outer-size-props {:outer-width :parent})
                 (map/use-default-values {:placeholder-text "••••••••"})
                 (map/use-default-values {:field-type (if password-visible? :text :password)})
                 (update :end-adornment-group update :adornments vector/conj-item toggle-visibility-adornment))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; @param (map) header
  ;
  ; @return (map)
  [id _ header]
  (let [field-id    (pretty-subitems/subitem-id id :field)
        field-error (form-validator/get-input-error field-id)]
       (-> header (map/use-default-values {:error-text field-error})
                  (map/use-default-values {:label      :password}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn props-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  [id props]
  (let [field-prototype-f  (fn [%] (field-prototype  id props %))
        header-prototype-f (fn [%] (header-prototype id props %))]
       (-> props (pretty-properties/default-flex-props       {:gap :xs :horizontal-align :left :orientation :vertical})
                 (pretty-properties/default-outer-size-props {:outer-size-unit :full-block})
                 (pretty-models/flex-container-standard-props)
                 (pretty-models/flex-container-rules)
                 (pretty-subitems/ensure-subitems          :field)
                 (pretty-subitems/subitems<-disabled-state :header :field)
                 (pretty-subitems/apply-subitem-prototype  :field  field-prototype-f)
                 (pretty-subitems/apply-subitem-prototype  :header header-prototype-f))))
