
(ns pretty-accessories.marker.prototypes
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
  (-> props (pretty-properties/default-background-color-props {:fill-color :default})
            (pretty-properties/default-outer-position-props   {:outer-position :tr :outer-position-method :absolute})
            (pretty-properties/default-outer-size-props       {:outer-height :xxs :outer-width :xxs :outer-size-unit :quarter-block})
            (pretty-models/plain-container-standard-props)
            (pretty-models/plain-container-rules)))
