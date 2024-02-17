
(ns pretty-accessories.tooltip.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn tooltip-props-prototype
  ; @ignore
  ;
  ; @param (keyword) tooltip-id
  ; @param (map) tooltip-props
  ;
  ; @return (map)
  [_ tooltip-props]
  (-> tooltip-props (pretty-properties/default-background-color-props {:fill-color :invert})
                    (pretty-properties/default-font-props             {:font-size :micro :font-weight :semi-bold})
                    (pretty-properties/default-position-props         {:position :right :position-base :outer :position-method :absolute :layer :uppermost})
                    (pretty-properties/default-size-props             {:size-unit :quarter-block})
                    (pretty-properties/default-text-props             {:text-color :invert :text-selectable? false})
                    (pretty-standards/standard-border-props)
                    (pretty-standards/standard-font-props)
                    (pretty-standards/standard-icon-props)
                    (pretty-standards/standard-text-props)
                    (pretty-standards/standard-wrapper-size-props)
                    ;(pretty-rules/auto-disable-highlight-color)
                    ;(pretty-rules/auto-disable-hover-color)
                    (pretty-rules/apply-auto-border-crop)
                    (pretty-rules/auto-adapt-wrapper-size)
                    (pretty-rules/compose-label)))
