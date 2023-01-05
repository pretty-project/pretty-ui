
(ns elements.image.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn image-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) image-props
  ;
  ; @return (map)
  ; {}
  [image-props]
  (merge {}
         (param image-props)))
