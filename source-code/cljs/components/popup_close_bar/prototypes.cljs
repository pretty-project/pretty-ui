
(ns components.popup-close-bar.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bar-props-prototype
  ; @param (map) bar-props
  ;
  ; @return (map)
  [{:keys [] :as bar-props}]
  (merge {}
         (param bar-props)))
