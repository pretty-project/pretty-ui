
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
  [label-props]
  (merge {:icon-family :material-icons-filled
          :style       {:max-width "248px"}}
         (param label-props)
         {:color            :muted
          :font-size        :xs
          :horizontal-align :left
          :indent           {:horizontal :xs :vertical :s}}))
