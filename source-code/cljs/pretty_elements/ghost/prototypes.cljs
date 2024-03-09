
(ns pretty-elements.ghost.prototypes
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
  (-> props (pretty-properties/default-animation-props        {:animation-duration 2000 :animation-mode :repeat :animation-name :pulse})
            (pretty-properties/default-background-color-props {:fill-color :highlight})
            (pretty-properties/default-outer-size-props       {:outer-height :s :outer-width :s :outer-size-unit :full-block})
            (pretty-models/plain-model-standard-props)
            (pretty-models/plain-model-rules)))
