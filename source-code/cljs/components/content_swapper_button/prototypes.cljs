
(ns components.content-swapper-button.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; @ignore
  ;
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {}
  [button-props]
  (merge {:font-size        :xs
          :gap              :auto
          :horizontal-align :left
          :hover-color      :highlight
          :icon             :chevron_right
          :icon-position    :right
          :icon-size        :m
          :indent           {:all :xs}
          :width            :auto}
         (param button-props)))
