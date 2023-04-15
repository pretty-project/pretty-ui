
(ns elements.content-swapper.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn swapper-props-prototype
  ; @ignore
  ;
  ; @param (map) swapper-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [swapper-props]
  ; If no threshold set, the default behaviour of the content swapper
  ; is swapping content independently from the screen width.
  (merge {:height    :content
          :threshold 9999
          :width     :content}
         (param swapper-props)))
