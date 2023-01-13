
(ns layouts.surface-a.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) surface-props
  ; {:content-orientation (keyword)}
  [surface-props]
  (merge {:content-orientation :vertical}
         (param surface-props)))
