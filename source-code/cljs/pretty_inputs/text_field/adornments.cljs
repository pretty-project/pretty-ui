
(ns pretty-inputs.text-field.adornments
    (:require [pretty-inputs.core.env :as core.env]
              [pretty-inputs.core.side-effects :as core.side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn empty-field-adornment
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ; {:disabled? (boolean)
  ;  :icon (keyword)
  ;  :on-click-f (function)
  ;  :tooltip (metamorphic-content)}
  [field-id field-props]
  (let [input-empty? (core.env/input-empty? field-id field-props)
        on-click-f   (fn [] (core.side-effects/empty-input! field-id field-props))]
       {:disabled?       input-empty?
        :on-click-f      on-click-f
        :icon            :close
        :tooltip-content :empty-field!}))
