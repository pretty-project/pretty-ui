
(ns components.list-item-button.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {}
  [button-props]
  (merge {:fill-color  :highlight
          :font-size   :xs
          :hover-color :highlight}
         (param button-props)))
