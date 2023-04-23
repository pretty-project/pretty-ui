
(ns elements.content-swapper.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn swapper-props-prototype
  ; @ignore
  ;
  ; @param (map) swapper-props
  ;
  ; @return (map)
  ; {}
  [swapper-props]
  (merge {:height :content
          :width  :content}
         (param swapper-props)))
