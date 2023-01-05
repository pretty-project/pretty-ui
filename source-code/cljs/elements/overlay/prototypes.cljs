
(ns elements.overlay.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn overlay-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) overlay-props
  ;
  ; @return (map)
  ; {}
  [overlay-props]
  (merge {}
         (param overlay-props)))
