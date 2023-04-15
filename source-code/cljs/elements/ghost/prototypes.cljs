
(ns elements.ghost.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost-props-prototype
  ; @ignore
  ;
  ; @param (map) ghost-props
  ;
  ; @return (map)
  ; {:height (keyword)
  ;  :width (keyword)}
  [ghost-props]
  (merge {:height :s
          :width  :auto}
         (param ghost-props)))
