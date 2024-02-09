
(ns pretty-diagrams.circle-diagram.prototypes
    (:require [pretty-properties.api :as pretty-properties]))

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
  (-> diagram-props (pretty-properties/default-data-props         {})
                    (pretty-properties/default-shape-props        {})
                    (pretty-properties/default-size-props         {:size-unit :full-block})
                    (pretty-properties/default-wrapper-size-props {})))
