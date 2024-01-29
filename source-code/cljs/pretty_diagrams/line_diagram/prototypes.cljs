
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
                    (core.props/row-props    {})
                    (core.props/stroke-props {:strength 2})))




;  (merge {:total-value (line-diagram.utils/diagram-props->total-value diagram-props)
;          :width :auto
;         (-> diagram-props)
;         (if strength {:strength (math/between! strength 1 6)}
;                      {:strength 2})))
