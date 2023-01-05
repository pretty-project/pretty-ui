
(ns elements.row.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn row-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) row-props
  ;
  ; @return (map)
  ; {:horizontal-align (keyword)
  ;  :stretch-orientation (keyword)
  ;  :vertical-align (keyword)
  ;  :wrap-items? (boolean)}
  [row-props]
  (merge {:horizontal-align    :left
          :stretch-orientation :horizontal
          :vertical-align      :center
          :wrap-items?         true}
         (param row-props)))
