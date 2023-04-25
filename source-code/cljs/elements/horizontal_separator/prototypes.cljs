
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
  ; {:color (keyword)
  ;  :width (keyword)}
  [separator-props]
  (merge {:color :default
          :width :auto}
         (param separator-props)))
