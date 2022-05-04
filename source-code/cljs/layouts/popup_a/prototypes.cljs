
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns layouts.popup-a.prototypes
    (:require [mid-fruits.candy :refer [param]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn layout-props-prototype
  [layout-props]
  (merge {:close-by-cover?     true
          :min-width           :none
          :stretch-orientation :none}
         (param layout-props)))
