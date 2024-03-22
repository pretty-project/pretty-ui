
(ns pretty-inputs.checkbox.prototypes
    (:require [form-validator.api    :as form-validator]
              [pretty-models.api     :as pretty-models]
              [pretty-properties.api :as pretty-properties]
              [pretty-subitems.api   :as pretty-subitems]))

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
  (let [option-group-id    (pretty-subitems/subitem-id id :option-group)
        option-group-error (form-validator/get-input-error option-group-id)]
       (-> header (assoc-in [:error-text :content] option-group-error))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn option-group-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; @param (map) option-group
  ;
  ; @return (map)
  [_ _ option-group]
  (-> option-group (pretty-properties/default-flex-props         {:gap :xs})
                   (pretty-properties/default-input-option-props {:option-default  {:gap :xs :icon {:border-color :muted :border-radius {:all :s} :border-width :xs :inner-height :xs :inner-width :xs}}})
                   (pretty-properties/default-input-option-props {:option-selected {:icon {:icon-name :done}}})))

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
  (let [header-prototype-f       (fn [%] (header-prototype       id props %))
        option-group-prototype-f (fn [%] (option-group-prototype id props %))]
       (-> props (pretty-properties/default-flex-props       {:gap :xs :horizontal-align :left :orientation :vertical})
                 (pretty-properties/default-outer-size-props {:outer-size-unit :full-block})
                 (pretty-models/flex-container-standard-props)
                 (pretty-models/flex-container-rules)
                 (pretty-subitems/subitem-group<-disabled-state :header :option-group)
                 (pretty-subitems/apply-subitem-prototype       :header       header-prototype-f)
                 (pretty-subitems/apply-subitem-prototype       :option-group option-group-prototype-f))))
