
(ns pretty-accessories.label.prototypes
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
  (-> props (pretty-properties/default-flex-props       {:orientation :horizontal})
            (pretty-properties/default-font-props       {:font-size :s :font-weight :medium})
            (pretty-properties/default-outer-size-props {:outer-size-unit :half-block})
            (pretty-properties/default-text-props       {:text-selectable? false})
            (pretty-models/content-model-standard-props)
            (pretty-models/content-model-rules)
            (pretty-models/text-model-standard-props)
            (pretty-models/text-model-rules)))
