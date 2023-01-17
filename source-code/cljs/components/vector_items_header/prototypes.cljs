
(ns components.vector-items-header.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-props-prototype
  ; @param (map) header-props
  ;
  ; @return (map)
  ; {:horizontal-align (keyword)
  ;  :initial-item (*)}
  [header-props]
  (merge {:horizontal-align :center
          :initial-item     {}}
         (param header-props)))
