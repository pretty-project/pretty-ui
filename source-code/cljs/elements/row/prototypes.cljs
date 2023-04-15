
(ns elements.row.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn row-props-prototype
  ; @ignore
  ;
  ; @param (map) row-props
  ; {}
  ;
  ; @return (map)
  ; {:border-position (keyword)
  ;  :border-width (keyword)
  ;  :height (keyword)
  ;  :horizontal-align (keyword)
  ;  :vertical-align (keyword)
  ;  :width (keyword)
  ;  :wrap-items? (boolean)}
  [{:keys [border-color] :as row-props}]
  (merge {:height           :content
          :horizontal-align :left
          :vertical-align   :center
          :width            :content
          :wrap-items?      true}
         (if border-color {:border-position :all
                           :border-width    :xxs})
         (param row-props)))
