
(ns pretty-diagrams.line-diagram.prototypes
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
  [diagram-id diagram-props]
  (-> diagram-props (core.props/data-props   {})
                    (core.props/row-props    {:horizontal-align :left})
                    (core.props/stroke-props {:strength 2})))
