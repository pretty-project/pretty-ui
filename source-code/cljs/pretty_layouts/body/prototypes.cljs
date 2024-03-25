
(ns pretty-layouts.body.prototypes
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
  (-> props (pretty-properties/default-content-size-props {:content-width :parent})
            (pretty-properties/default-flex-props         {:orientation :horizontal})
            (pretty-properties/default-outer-size-props   {:outer-height :content :outer-width :parent :outer-size-unit :full-block})
            (pretty-models/flex-container-standard-props)
            (pretty-models/flex-container-rules)
            (pretty-models/plain-content-standard-props)
            (pretty-models/plain-content-rules)))
