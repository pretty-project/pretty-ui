
(ns components.surface-title.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn title-props-prototype
  ; @param (map) title-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [{:keys [] :as title-props}]
  (merge {}
         (param title-props)))
