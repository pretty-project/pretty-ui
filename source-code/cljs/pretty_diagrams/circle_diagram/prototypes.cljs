
(ns pretty-diagrams.circle-diagram.prototypes
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
  [_ props]
  (-> props (pretty-properties/default-outer-size-props {:outer-size-unit :full-block})
            (pretty-models/container-model-standard-props)
            (pretty-models/container-model-rules)
            (pretty-models/shape-model-standard-props)
            (pretty-models/shape-model-rules)))
