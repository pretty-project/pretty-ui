
(ns elements.row.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn row-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) row-props
  ; {}
  ;
  ; @return (map)
  ; {:border-position (keyword)
  ;  :border-width (keyword)
  ;  :horizontal-align (keyword)
  ;  :stretch-orientation (keyword)
  ;  :vertical-align (keyword)
  ;  :wrap-items? (boolean)}
  [{:keys [border-color] :as row-props}]
  (merge {:horizontal-align    :left
          :stretch-orientation :horizontal
          :vertical-align      :center
          :wrap-items?         true}
         (if border-color {:border-position :all
                           :border-width    :xxs})
         (param row-props)))
