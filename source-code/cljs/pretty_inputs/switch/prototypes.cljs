
(ns pretty-inputs.switch.prototypes
    (:require [pretty-standards.api :as pretty-standards]
              [pretty-rules.api :as pretty-rules]
              [pretty-properties.api :as pretty-properties]
              [pretty-subitems.api :as pretty-subitems]
              [fruits.map.api :as map]))

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
  (-> option-group (update :option-selected map/reversed-deep-merge {:icon {:horizontal-align :right}})
                   (update :option-default  map/reversed-deep-merge {:icon {:icon-color   :muted :icon-name :circle
                                                                            :border-color :muted :border-radius {:all :l} :border-width :xs
                                                                            :inner-height :xs :inner-width :m
                                                                            :horizontal-align :left}})))

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
                 (pretty-standards/standard-flex-props)
                 (pretty-standards/standard-inner-position-props)
                 (pretty-standards/standard-inner-size-props)
                 (pretty-standards/standard-outer-position-props)
                 (pretty-standards/standard-outer-size-props)
                ;(pretty-rules/auto-align-scrollable-flex)
                 (pretty-rules/auto-set-mounted)
                 (pretty-subitems/subitem-group<-disabled-state :header :option-group)
                 (pretty-subitems/leave-disabled-state          :header :option-group)
                 (pretty-subitems/apply-subitem-prototype       :option-group option-group-prototype-f))))
