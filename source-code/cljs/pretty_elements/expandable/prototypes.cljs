
(ns pretty-elements.expandable.prototypes
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
  (-> props (pretty-properties/default-outer-size-props {:outer-size-unit :double-block})
            (pretty-properties/default-text-props       {:text-selectable? false})
            (pretty-models/container-model-standard-props)
            (pretty-models/container-model-rules)
            (pretty-models/content-model-standard-props)
            (pretty-models/content-model-rules)))
