
(ns pretty-diagrams.bar-diagram.prototypes
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
  [_ diagram-props]
  (-> diagram-props (pretty-diagrams.properties/default-data-props  {})
                    (pretty-diagrams.properties/default-shape-props {})))
