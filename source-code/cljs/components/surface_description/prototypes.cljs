
(ns components.surface-description.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn description-props-prototype
  ; @param (map) description-props
  ;
  ; @return (map)
  ; {:font-size (keyword)
  ;  :horizontal-align (keyword)
  ;  :indent (map)}
  [description-props]
  (merge {:font-size        :xxs
          :horizontal-align :center
          :indent           {:horizontal :xs :vertical :xs}}
         (param description-props)))
