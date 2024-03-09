
(ns pretty-inputs.checkbox.prototypes
    (:require [fruits.map.api        :as map]
              [pretty-properties.api :as pretty-properties]
              [pretty-models.api :as pretty-models]
              [pretty-subitems.api   :as pretty-subitems]))

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
  (-> option-group (pretty-properties/default-flex-props {:gap :xs})
                   (update :option-default  map/reversed-deep-merge {:gap :xs :icon {:border-color :muted :border-radius {:all :s} :border-width :xs :inner-height :xs :inner-width :xs}})
                   (update :option-selected map/reversed-deep-merge {         :icon {:icon-name :done}})))

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
                 (pretty-models/container-model-standard-props)
                 (pretty-models/container-model-rules)
                 (pretty-subitems/subitem-group<-disabled-state :header :option-group)
                 (pretty-subitems/apply-subitem-prototype       :option-group option-group-prototype-f))))
