
(ns elements.horizontal-polarity.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn polarity-props-prototype
  ; @ignore
  ;
  ; @param (map) polarity-props
  ;
  ; @return (map)
  ; {:vertical-align (keyword)
  ;  :width (keyword)}
  [polarity-props]
  (merge {:vertical-align :center
          :width          :auto}
         (param polarity-props)))
