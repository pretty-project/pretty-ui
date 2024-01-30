
(ns pretty-diagrams.line-diagram.prototypes
    (:require [pretty-diagrams.properties.api :as pretty-diagrams.properties]))

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
  (-> diagram-props (pretty-diagrams.properties/default-data-props  {})
                    (pretty-diagrams.properties/default-flex-props  {:horizontal-align :left :orientation :horizontal})
                    (pretty-diagrams.properties/default-shape-props {})))
