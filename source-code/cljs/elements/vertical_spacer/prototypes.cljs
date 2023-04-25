
(ns elements.vertical-spacer.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn spacer-props-prototype
  ; @ignore
  ;
  ; @param (map) spacer-props
  ;
  ; @return (map)
  ; {:width (keyword)}
  [spacer-props]
  (merge {:width :s}
         (param spacer-props)))
