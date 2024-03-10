
(ns pretty-diagrams.point-diagram.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-models.api :as pretty-models]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn props-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  [_ props]
  (-> props (pretty-properties/default-outer-size-props {:outer-size-unit :full-block})
            (pretty-models/plain-container-standard-props)
            (pretty-models/plain-container-rules)
            (pretty-models/shape-canvas-standard-props)
            (pretty-models/shape-canvas-rules)))
