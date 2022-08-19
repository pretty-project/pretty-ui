
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.color-marker.prototypes
    (:require [mid-fruits.candy :refer [param]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn marker-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) marker-props
  ;
  ; @return (map)
  ;  {:colors (keywords or strings in vector)
  ;   :size (keyword)}
  [marker-props]
  (merge {:colors [:highlight]
          :size   :s}
         (param marker-props)))
