
(ns components.illustration.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn illustration-props-prototype
  ; @param (map) illustration-props
  ;
  ; @return (map)
  ; {:position (keyword)
  ;  :size (keyword)}
  [illustration-props]
  (merge {:position :br
          :size     :xxl}
         (param illustration-props)))
