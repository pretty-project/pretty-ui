
(ns elements.vertical-group.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn group-props-prototype
  ; @ignore
  ;
  ; @param (map) group-props
  ;
  ; @return (map)
  ; {:width (keyword)}
  [group-props]
  (merge {:width :content}
         (param group-props)))
