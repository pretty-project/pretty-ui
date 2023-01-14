
(ns components.vector-items-header.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-props-prototype
  ; @param (map) header-props
  ;
  ; @return (map)
  ; {:initial-item (*)}
  [header-props]
  (merge {:initial-item {}}
         (param header-props)))
