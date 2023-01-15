
(ns components.side-menu-button.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {}
  [button-props]
  (merge {:font-size        :xs
          :horizontal-align :left
          :hover-color      :highlight
          :icon-size        :m
          :indent           {:horizontal :xs :left :s :right :xl}}
         (param button-props)))
