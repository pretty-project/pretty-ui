
(ns elements.card.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn card-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) card-props
  ;
  ; @return (map)
  ; {:border-radius (keyword)
  ;  :horizontal-align (keyword)}
  [card-props]
  (merge {:border-radius    :s
          :horizontal-align :center}
         (param card-props)))
