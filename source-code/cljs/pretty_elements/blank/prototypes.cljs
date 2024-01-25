
(ns pretty-elements.blank.prototypes
    (:require [pretty-elements.core.props :as core.props]))

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
  (-> blank-props (core.props/content-props {})))
