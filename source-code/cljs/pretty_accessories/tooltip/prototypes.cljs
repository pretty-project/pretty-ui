
(ns pretty-accessories.tooltip.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api      :as pretty-rules]
              [pretty-standards.api  :as pretty-standards]
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
  (-> label (pretty-properties/default-font-props {:font-size :micro :font-weight :semi-bold})
            (pretty-properties/default-text-props {:text-color :invert})))

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
       (-> props (pretty-properties/default-background-color-props {:fill-color :invert})
                 (pretty-properties/default-outer-position-props   {:outer-position :right :outer-position-base :external :outer-position-method :absolute :layer :uppermost})
                 (pretty-properties/default-outer-size-props       {:outer-size-unit :quarter-block})
                 (pretty-standards/standard-animation-props)
                 (pretty-standards/standard-border-props)
                 (pretty-standards/standard-inner-position-props)
                 (pretty-standards/standard-inner-size-props)
                 (pretty-standards/standard-outer-position-props)
                 (pretty-standards/standard-outer-size-props)
                 (pretty-rules/apply-auto-border-crop)
                 (pretty-subitems/apply-subitem-prototype :label label-prototype-f))))
