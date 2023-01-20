
(ns components.popup-menu-label.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-props-prototype
  ; @param (map) label-props
  ;
  ; @return (map)
  ; {:color (keyword)
  ;  :gap (keyword)
  ;  :horizontal-align (keyword)
  ;  :icon-size (keyword)
  ;  :indent (map)}
  [label-props]
  (merge {:color            :muted
          :gap              :xs
          :horizontal-align :left
          :icon-size        :m
          :indent           {:horizontal :xxs :vertical :s}}
         (param label-props)))
