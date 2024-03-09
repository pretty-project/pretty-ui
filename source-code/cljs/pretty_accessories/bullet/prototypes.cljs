
(ns pretty-accessories.bullet.prototypes
    (:require [pretty-models.api     :as pretty-models]
              [pretty-properties.api :as pretty-properties]))

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
  (-> props (pretty-properties/default-background-color-props {:fill-color :muted})
            (pretty-properties/default-outer-size-props       {:outer-height :xxs :outer-width :xxs :outer-size-unit :quarter-block})
            (pretty-models/plain-model-standard-props)
            (pretty-models/plain-model-rules)))
