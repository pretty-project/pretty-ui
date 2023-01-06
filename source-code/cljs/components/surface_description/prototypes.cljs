
(ns components.surface-description.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn description-props-prototype
  ; @param (map) description-props
  ;
  ; @return (map)
  ; {:color (keyword)
  ;  :font-size (keyword)
  ;  :horizontal-align (keyword)
  ;  :indent (map)
  ;  :line-height (keyword)}
  [description-props]
  (merge {:horizontal-align :center
          :indent           {:horizontal :xs :vertical :xs}}
         (param description-props)
         {:color       :muted
          :font-size   :xxs
          :line-height :block}))
