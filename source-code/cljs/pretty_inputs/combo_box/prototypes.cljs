
(ns pretty-inputs.combo-box.prototypes
    (:require [form-validator.api       :as form-validator]
              [fruits.string.api        :as string]
              [pretty-inputs.engine.api :as pretty-inputs.engine]
              [pretty-models.api        :as pretty-models]
              [pretty-properties.api    :as pretty-properties]
              [pretty-subitems.api      :as pretty-subitems]))

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
       (-> header (assoc-in [:error-text :content] field-error))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn option-group-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:field (map)(opt)
  ;  ...}
  ; @param (map) option-group
  ;
  ; @return (map)
  [id {:keys [field]} option-group]
  (let [field-id         (pretty-subitems/subitem-id id :field)
        field-content    (pretty-inputs.engine/get-input-field-displayed-content field-id field)
        option-compare-f (fn [%] (string/starts-with? % field-content {:case-sensitive? false}))
        option-filter-f  (fn [%] (-> % :label :content option-compare-f))]
       (-> option-group (pretty-properties/default-input-option-props {:option-filter-f option-filter-f})
                        (pretty-properties/default-outer-size-props   {:outer-width :parent}))))

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
                 (pretty-subitems/ensure-subitems          :field)
                 (pretty-subitems/subitems<-disabled-state :header :field :option-group)
                 (pretty-subitems/apply-subitem-prototype  :header       header-prototype-f)
                 (pretty-subitems/apply-subitem-prototype  :option-group option-group-prototype-f))))
