
(ns components.copyright-label.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-props-prototype
  ; @param (map) label-props
  ;
  ; @return (map)
  ; {:font-size (keyword)}
  [label-props]
  (merge {:font-size :xxs}
         (param label-props)))
