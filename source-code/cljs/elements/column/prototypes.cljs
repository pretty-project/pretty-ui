
(ns elements.column.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn column-props-prototype
  ; @ignore
  ;
  ; @param (map) column-props
  ; {}
  ;
  ; @return (map)
  ; {:border-position (keyword)
  ;  :border-width (keyword)
  ;  :horizontal-align (keyword)
  ;  :stretch-orientation (keyword)
  ;  :vertical-align (keyword)}
  [{:keys [border-color] :as column-props}]
  (merge {:horizontal-align    :center
          :stretch-orientation :vertical
          :vertical-align      :top}
         (if border-color {:border-position :all
                           :border-width    :xxs})
         (param column-props)))
