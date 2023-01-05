
(ns components.list-item-button.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {}
  [button-props]
  (merge {:background-color :highlight
          :font-size        :xs
          :hover-color      :highlight}
         (param button-props)))
