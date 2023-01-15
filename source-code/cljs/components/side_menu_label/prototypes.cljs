
(ns components.side-menu-label.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-props-prototype
  ; @param (map) label-props
  ;
  ; @return (map)
  ; {:color (keyword)
  ;  :font-size (keyword)
  ;  :horizontal-align (keyword)
  ;  :indent (map)
  ;  :style (map)}
  [label-props]
  (merge {:color            :muted
          :font-size        :xs
          :horizontal-align :left
          :indent           {:horizontal :xs :vertical :s}
          :style {:max-width "var(--element-size-m)"}}
         (param label-props)))
