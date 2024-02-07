
(ns pretty-elements.blank.prototypes
    (:require [pretty-properties.api :as pretty-properties]))

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
  (-> blank-props (pretty-properties/default-content-props {})))
