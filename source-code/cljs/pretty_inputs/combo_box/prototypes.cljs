
(ns pretty-inputs.combo-box.prototypes
    (:require [fruits.string.api        :as string]
              [pretty-inputs.engine.api :as pretty-inputs.engine]
              [pretty-properties.api    :as pretty-properties]
              [pretty-models.api :as pretty-models]
              [pretty-subitems.api      :as pretty-subitems]))

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
  (let [option-group-prototype-f (fn [%] (option-group-prototype id props %))]
       (-> props (pretty-properties/default-flex-props       {:gap :xs :horizontal-align :left :orientation :vertical})
                 (pretty-properties/default-outer-size-props {:outer-size-unit :full-block})
                 (pretty-models/flex-container-standard-props)
                 (pretty-models/flex-container-rules)
                 (pretty-subitems/ensure-subitem           :field)
                 (pretty-subitems/subitems<-disabled-state :header :field :option-group)
                 (pretty-subitems/apply-subitem-prototype  :option-group option-group-prototype-f))))
