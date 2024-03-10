
(ns pretty-elements.toggle.prototypes
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
  (-> props (pretty-properties/default-flex-props         {:orientation :horizontal})
            (pretty-properties/default-content-size-props {:content-height :parent :content-width :parent})
            (pretty-properties/default-outer-size-props   {:outer-size-unit :double-block})
            (pretty-models/click-control-standard-props)
            (pretty-models/click-control-rules)
            (pretty-models/flex-content-standard-props)
            (pretty-models/flex-content-rules)
            (pretty-models/plain-container-standard-props)
            (pretty-models/plain-container-rules)))
