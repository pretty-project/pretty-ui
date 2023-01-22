
(ns components.sidebar-button.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; @ignore
  ;
  ; @param (map) button-props
  ; {:icon (keyword)(opt)}
  ;
  ; @return (map)
  ; {}
  [{:keys [icon] :as button-props}]
  (merge {:color            :invert
          :font-size        :xs
          :font-weight      :medium
          :gap              :xs
          :horizontal-align :left
          :hover-color      :invert
          :indent {:left :s :right :xl :horizontal :xs}}
         (if icon {:icon-size :m})
         (param button-props)))
