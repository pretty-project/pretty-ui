
(ns pretty-accessories.tooltip.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-models.api      :as pretty-models]
              [pretty-subitems.api   :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; @param (map) label
  ;
  ; @return (map)
  [_ _ label]
  (-> label (pretty-properties/default-font-props {:font-size :micro :font-weight :semi-bold})))

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
  (let [label-prototype-f (fn [%] (label-prototype id props %))]
       (-> props (pretty-properties/default-background-color-props {:fill-color :default})
                 (pretty-properties/default-outer-position-props   {:outer-position :right :outer-position-base :external :outer-position-method :absolute :layer :uppermost})
                 (pretty-properties/default-outer-size-props       {:outer-size-unit :quarter-block})
                 (pretty-models/content-model-standard-props)
                 (pretty-models/content-model-rules)
                 (pretty-subitems/apply-subitem-prototype :label label-prototype-f))))
