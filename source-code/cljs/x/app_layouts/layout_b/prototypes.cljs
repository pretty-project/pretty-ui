
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-layouts.layout-b.prototypes
    (:require [mid-fruits.candy :refer [param]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn layout-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) layout-props
  ;
  ; @return (map)
  ;  {:horizontal-align (keyword)}
  [layout-props]
  (merge {:horizontal-align :center}
         (param layout-props)))
