
(ns elements.ghost.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost-props-prototype
  ; @ignore
  ;
  ; @param (map) ghost-props
  ;
  ; @return (map)
  ; {:height (keyword)}
  [ghost-props]
  (merge {:height :s}
         (param ghost-props)))
