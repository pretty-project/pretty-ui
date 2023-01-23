
(ns elements.vertical-separator.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn separator-props-prototype
  ; @ignore
  ;
  ; @param (map) separator-props
  ;
  ; @return (map)
  ; {:width (keyword)}
  [separator-props]
  (merge {:width :s}
         (param separator-props)))
