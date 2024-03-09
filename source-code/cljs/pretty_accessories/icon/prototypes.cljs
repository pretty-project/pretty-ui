
(ns pretty-accessories.icon.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-models.api  :as pretty-models]))

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
  (-> props (pretty-properties/default-flex-props       {:orientation :horizontal})
            (pretty-properties/default-icon-props       {:icon-size :s})
            (pretty-properties/default-outer-size-props {:outer-size-unit :half-block})
            (pretty-models/container-model-standard-props)
            (pretty-models/container-model-rules)
            (pretty-models/icon-model-standard-props)
            (pretty-models/icon-model-rules)))
