
(ns elements.blank.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn blank-props-prototype
  ; @ignore
  ;
  ; @param (map) blank-props
  ;
  ; @return (map)
  ; {:width (keyword)}
  [blank-props]
  (merge {:width :content}
         (param blank-props)))
