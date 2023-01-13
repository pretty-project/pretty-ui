
(ns layouts.popup-b.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) popup-props
  [popup-props]
  (merge {:close-by-cover? true}
         (param popup-props)))
