
(ns pretty-elements.vertical-spacer.prototypes
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
  (-> props (pretty-properties/default-flex-props       {:shrink 0})
            (pretty-properties/default-outer-size-props {:outer-height :parent :outer-width :s :outer-size-unit :quarter-block})
            (pretty-models/plain-container-standard-props)
            (pretty-models/plain-container-rules)))
