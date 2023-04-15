
(ns elements.vertical-polarity.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn polarity-props-prototype
  ; @ignore
  ;
  ; @param (map) polarity-props
  ;
  ; @return (map)
  ; {:height (keyword)
  ;  :horizontal-align (keyword)}
  [polarity-props]
  (merge {:height           :parent
          :horizontal-align :center}
         (param polarity-props)))
