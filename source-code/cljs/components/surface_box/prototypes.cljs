
(ns components.surface-box.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn box-props-prototype
  ; @param (map) box-props
  ; {}
  ;
  ; @return (map)
  ; {:fill-color (string)}
  [{:keys [] :as box-props}]
  (merge {:fill-color "var( --fill-color-default )"}
         (param box-props)))
