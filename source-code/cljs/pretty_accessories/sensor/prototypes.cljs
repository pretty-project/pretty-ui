
(ns pretty-accessories.sensor.prototypes
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
  (-> props (pretty-properties/default-outer-position-props {:outer-position :left :outer-position-method :absolute})
            (pretty-properties/default-outer-size-props     {:outer-height :parent :outer-width :s :outer-size-unit :full-block})
            (pretty-models/plain-container-standard-props)
            (pretty-models/plain-container-rules)))
