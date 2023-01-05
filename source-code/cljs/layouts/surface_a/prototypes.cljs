
(ns layouts.surface-a.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn layout-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) layout-props
  ; {:content-orientation (keyword)}
  [layout-props]
  (merge {:content-orientation :vertical}
         (param layout-props)))
