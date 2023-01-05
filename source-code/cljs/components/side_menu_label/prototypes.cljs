
(ns components.side-menu-label.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-props-prototype
  ; @param (map) label-props
  ;
  ; @return (map)
  ; {:color (keyword)
  ;  :font-size (keyword)
  ;  :horizontal-align (keyword)
  ;  :icon (keyword)
  ;  :icon-family (keyword)
  ;  :indent (map)}
  [{:keys [] :as label-props}]
  (merge {:color            :muted
          :font-size        :xs
          :horizontal-align :left
          :icon-family      :material-icons-filled
          :indent           {:horizontal :xs :vertical :s}
          :line-height :block
          :style {:max-width "248px"}}
         (param label-props)))
