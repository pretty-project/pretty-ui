
(ns components.side-menu-label.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-props-prototype
  ; @ignore
  ;
  ; @param (map) label-props
  ;
  ; @return (map)
  ; {:font-size (keyword)
  ;  :gap (keyword)
  ;  :horizontal-align (keyword)
  ;  :icon-size (keyword)
  ;  :indent (map)
  ;  :style (map)}
  [label-props]
  (merge {:font-size        :xs
          :gap              :xs
          :horizontal-align :left
          :icon-size        :m
          :indent           {:horizontal :xs :vertical :s}
          :style {:max-width "var(--element-size-m)"}}
         (param label-props)))
