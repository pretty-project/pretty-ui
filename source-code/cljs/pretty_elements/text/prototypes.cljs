
(ns pretty-elements.text.prototypes
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
  ; @bug (pretty-elements.header.prototypes#9811)
  (-> props (pretty-properties/default-content-props    {:content-placeholder "\u00A0"})
            (pretty-properties/default-flex-props       {:orientation :horizontal})
            (pretty-properties/default-font-props       {:font-size :s :font-weight :normal})
            (pretty-properties/default-outer-size-props {:outer-size-unit :full-block})
            (pretty-properties/default-text-props       {:text-overflow :wrap})
            (pretty-models/container-model-standard-props)
            (pretty-models/container-model-rules)
            (pretty-models/content-model-standard-props)
            (pretty-models/content-model-rules)
            (pretty-models/multiline-model-standard-props)
            (pretty-models/multiline-model-rules)))
