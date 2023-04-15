
(ns elements.image.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn image-props-prototype
  ; @ignore
  ;
  ; @param (map) image-props
  ;
  ; @return (map)
  ; {}
  [image-props]
  (merge {:height :content
          :width  :content}
         (param image-props)))
