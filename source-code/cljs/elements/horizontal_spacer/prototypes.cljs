
(ns elements.horizontal-spacer.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn spacer-props-prototype
  ; @ignore
  ;
  ; @param (map) spacer-props
  ;
  ; @return (map)
  ; {:height (keyword)}
  [spacer-props]
  (merge {:height :s}
         (param spacer-props)))
