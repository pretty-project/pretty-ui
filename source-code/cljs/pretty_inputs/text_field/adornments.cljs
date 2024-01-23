
(ns pretty-inputs.text-field.adornments
    (:require [pretty-engine.api :as pretty-engine]))

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
  (let [input-empty? (pretty-engine/input-empty? field-id field-props)
        on-click-f   (fn [] (pretty-engine/empty-input! field-id field-props))]
       {:disabled?       input-empty?
        :on-click-f      on-click-f
        :icon            :close
        :tooltip-content :empty-field!}))
