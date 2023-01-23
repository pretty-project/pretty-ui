
(ns components.list-item-cell.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn cell-props-prototype
  ; @param (map) cell-props
  ;
  ; @return (map)
  [_])

(defn row-props-prototype
  ; @param (map) cell-props
  ;
  ; @return (map)
  [cell-props]
  (merge {:indent {:vertical :xs}
          :selectable? true}
         (param cell-props)))
