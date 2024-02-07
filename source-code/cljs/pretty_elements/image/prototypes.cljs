
(ns pretty-elements.image.prototypes
    (:require [pretty-properties.api :as pretty-properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn image-props-prototype
  ; @ignore
  ;
  ; @param (keyword) image-id
  ; @param (map) image-props
  ;
  ; @return (map)
  [_ image-props]
  (-> image-props (pretty-properties/default-size-props {:height :s :width :s})))
