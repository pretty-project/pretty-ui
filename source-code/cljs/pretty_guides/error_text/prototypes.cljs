
(ns pretty-guides.error-text.prototypes
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
  (-> props (pretty-properties/default-font-props       {:font-size :xs :font-weight :normal})
            (pretty-properties/default-outer-size-props {:outer-size-unit :quarter-block})
            (pretty-properties/default-text-props       {:text-color :warning :text-selectable? true})
            (pretty-models/flex-container-standard-props)
            (pretty-models/flex-container-rules)
            (pretty-models/plain-content-standard-props)
            (pretty-models/plain-content-rules)))
