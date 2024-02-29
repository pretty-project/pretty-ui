
(ns pretty-accessories.tooltip.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]
              [pretty-subitems.api :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-props-prototype
  ; @ignore
  ;
  ; @param (keyword) badge-id
  ; @param (map) badge-props
  ; {:label (map)(opt)
  ;  ...}
  ;
  ; @return (map)
  [_ {:keys [label]}]
  (-> label (pretty-properties/default-font-props {:font-size :micro :font-weight :semi-bold})
            (pretty-properties/default-text-props {:text-color :invert :text-selectable? false})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn tooltip-props-prototype
  ; @ignore
  ;
  ; @param (keyword) tooltip-id
  ; @param (map) tooltip-props
  ;
  ; @return (map)
  [tooltip-id tooltip-props]
  (let [label-props-prototype-f (fn [_] (label-props-prototype tooltip-id tooltip-props))]
       (-> tooltip-props (pretty-properties/default-background-color-props {:fill-color :invert})
                         (pretty-properties/default-outer-position-props   {:outer-position :right :outer-position-base :external :outer-position-method :absolute :layer :uppermost})
                         (pretty-properties/default-outer-size-props       {:outer-size-unit :quarter-block})
                         (pretty-standards/standard-animation-props)
                         (pretty-standards/standard-border-props)
                         (pretty-standards/standard-inner-position-props)
                         (pretty-standards/standard-inner-size-props)
                         (pretty-standards/standard-outer-position-props)
                         (pretty-standards/standard-outer-size-props)
                         (pretty-rules/apply-auto-border-crop)
                        ;(pretty-rules/auto-disable-highlight-color)
                        ;(pretty-rules/auto-disable-hover-color)
                         (pretty-subitems/apply-subitem-prototype :label label-props-prototype-f))))
