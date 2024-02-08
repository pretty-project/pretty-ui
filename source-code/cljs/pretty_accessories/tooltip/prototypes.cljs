
(ns pretty-accessories.tooltip.prototypes
    (:require [pretty-properties.api :as pretty-properties]))

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
                    (pretty-properties/default-border-props           {})
                    (pretty-properties/default-font-props             {:font-size :micro :font-weight :semi-bold})
                    (pretty-properties/default-icon-props             {})
                    (pretty-properties/default-label-props            {})
                    (pretty-properties/default-position-props         {:position :right :position-base :outer :position-method :absolute :layer :uppermost})
                    (pretty-properties/default-space-props            {:indent {:all :xxs}})
                    (pretty-properties/default-text-props             {:text-color :invert :text-selectable? false})))
