
(ns components.sidebar-button.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {}
  [button-props]
  (merge {:hover-color :invert
          :icon-family :material-icons-filled}
         (param button-props)
         {:color            :invert
          :font-size        :xs
          :font-weight      :bold
          :line-height      :block
          :horizontal-align :left
          :icon-size        :s
          :indent {:left :s :right :xl :horizontal :xs}}))
