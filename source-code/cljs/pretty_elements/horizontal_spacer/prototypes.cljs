
(ns pretty-elements.horizontal-spacer.prototypes
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
  (-> props (pretty-properties/default-flex-props       {:shrink 0})
            (pretty-properties/default-outer-size-props {:outer-height :s :outer-width :auto :outer-size-unit :quarter-block})
            (pretty-models/plain-container-standard-props)
            (pretty-models/plain-container-rules)))
