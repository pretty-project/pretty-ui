
(ns pretty-diagrams.circle-diagram.prototypes
    (:require [pretty-diagrams.core.props :as core.props]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-props-prototype
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;
  ; @return (map)
  [_ diagram-props]
  (-> diagram-props (core.props/data-props  {})
                    (core.props/shape-props {})))
