
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.ghost.prototypes
    (:require [mid-fruits.candy :refer [param]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) ghost-props
  ;
  ; @return (map)
  ;  {:height (keyword)}
  [ghost-props]
  (merge {:height :s}
         (param ghost-props)))
