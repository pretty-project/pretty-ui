
(ns pretty-accessories.sensor.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-standards.api :as pretty-standards]
              [pretty-rules.api :as pretty-rules]))

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
            (pretty-standards/standard-animation-props)
            (pretty-standards/standard-inner-position-props)
            (pretty-standards/standard-inner-size-props)
            (pretty-standards/standard-outer-position-props)
            (pretty-standards/standard-outer-size-props)
            (pretty-rules/auto-blur-click-events)
            (pretty-rules/auto-disable-mouse-events)))
