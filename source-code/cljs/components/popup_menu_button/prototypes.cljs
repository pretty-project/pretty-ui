
(ns components.popup-menu-button.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {}
  [button-props]
  (merge {:border-radius    {:all :s}
          :font-size        :xs
          :gap              :xs
          :horizontal-align :left
          :hover-color      :highlight
          :icon-size        :m
          :indent           {:horizontal :xxs :vertical :xxs}
          :outdent          {:vertical :xs}}
         (param button-props)))
