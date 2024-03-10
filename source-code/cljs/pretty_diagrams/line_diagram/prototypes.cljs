
(ns pretty-diagrams.line-diagram.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-models.api      :as pretty-models]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn props-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  [id props]
  (-> props (pretty-properties/default-flex-props       {:horizontal-align :left :orientation :horizontal})
            (pretty-properties/default-outer-size-props {:outer-size-unit :full-block})
            (pretty-models/flex-container-standard-props)
            (pretty-models/flex-container-rules)
            (pretty-models/shape-canvas-standard-props)
            (pretty-models/shape-canvas-rules)))
