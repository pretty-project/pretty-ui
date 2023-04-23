
(ns components.content-swapper-header.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-props-prototype
  ; @ignore
  ;
  ; @param (map) header-props
  ;
  ; @return (map)
  ; {}
  [header-props]
  (merge {:border-color     :highlight
          :border-position  :bottom
          :font-size        :xs
          :gap              :xs
          :horizontal-align :left
          :hover-color      :highlight
          :icon             :chevron_left
          :icon-position    :left
          :icon-size        :xl
          :indent           {:horizontal :xs}
          :width            :auto}
         (param header-props)))
