
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
  ;  {:border-radius (keyword)
  ;   :height (keyword)}
  [ghost-props]
  (merge {:border-radius :s
          :height        :s}
         (param ghost-props)))
