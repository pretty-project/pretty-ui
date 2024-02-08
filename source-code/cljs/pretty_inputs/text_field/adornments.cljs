
(ns pretty-inputs.text-field.adornments
    (:require [pretty-inputs.engine.api :as pretty-inputs.engine]))

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
  ;  :on-click-f (function)}
  [field-id field-props]
  (let [input-empty? (pretty-inputs.engine/input-empty? field-id field-props)
        on-click-f   (fn [] (pretty-inputs.engine/empty-input! field-id field-props))]
       {:disabled?  input-empty?
        :on-click-f on-click-f
        :icon       :close}))
