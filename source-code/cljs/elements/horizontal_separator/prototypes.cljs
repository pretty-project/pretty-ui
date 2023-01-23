
(ns elements.horizontal-separator.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn separator-props-prototype
  ; @ignore
  ;
  ; @param (map) separator-props
  ;
  ; @return (map)
  ; {:height (keyword)}
  [separator-props]
  (merge {:height :s}
         (param separator-props)))
