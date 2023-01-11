
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
  (merge {:icon-family :material-icons-filled}
         (param button-props)
         {:font-size        :xs
          :hover-color      :highlight
          :horizontal-align :left
          :icon-size        :s
          :indent           {:horizontal :xs :left :s :right :xl}}))
