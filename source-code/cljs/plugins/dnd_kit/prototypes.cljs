
(ns plugins.dnd-kit.prototypes
    (:require [mid-fruits.candy :refer [param return]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sortable-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) sortable-props
  ;
  ; @return (map)
  ;  {:item-id-f (function)}
  [sortable-props]
  (merge {:item-id-f return}
         (param sortable-props)))
