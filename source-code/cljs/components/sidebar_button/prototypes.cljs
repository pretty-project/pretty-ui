
(ns components.sidebar-button.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; @param (map) button-props
  ; {:icon (keyword)(opt)}
  ;
  ; @return (map)
  ; {}
  [{:keys [icon] :as button-props}]
  (merge {:hover-color :invert}
         (param button-props)
         (if icon {:icon-size :s})
         {:color            :invert
          :font-size        :xs
          :font-weight      :medium
          :horizontal-align :left
          :indent {:left :s :right :xl :horizontal :xs}}))
