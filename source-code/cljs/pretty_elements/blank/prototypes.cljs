
(ns pretty-elements.blank.prototypes
    (:require [pretty-elements.properties.api :as pretty-elements.properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn blank-props-prototype
  ; @ignore
  ;
  ; @param (keyword) blank-id
  ; @param (map) blank-props
  ;
  ; @return (map)
  [_ blank-props]
  (-> blank-props (pretty-elements.properties/default-content-props {})))
