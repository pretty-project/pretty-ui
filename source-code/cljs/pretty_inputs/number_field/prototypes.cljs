
(ns pretty-inputs.number-field.prototypes
    (:require [form-validator.api    :as form-validator]
              [pretty-models.api     :as pretty-models]
              [pretty-properties.api :as pretty-properties]
              [pretty-subitems.api   :as pretty-subitems]
              [fruits.map.api :as map]))

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
  [_ _ field]
  (-> field (pretty-properties/default-outer-size-props {:outer-width :parent})
            (map/use-default-values {:field-type :number})))

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
       (-> header (map/use-default-values {:error-text field-error}))))

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
