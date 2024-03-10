
(ns pretty-tables.cell.prototypes
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
  (-> props (pretty-properties/default-font-props       {:font-size :xxs :font-weight :normal})
            (pretty-properties/default-flex-props       {:horizontal-align :center :orientation :horizontal})
            (pretty-properties/default-outer-size-props {:outer-height :content :outer-width :auto :outer-size-unit :full-block})
            (pretty-properties/default-text-props       {:text-overflow :ellipsis})
            (pretty-models/flex-container-standard-props)
            (pretty-models/flex-container-rules)
            (pretty-models/plain-content-standard-props)
            (pretty-models/plain-content-rules)))
