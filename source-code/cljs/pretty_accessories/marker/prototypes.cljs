
(ns pretty-accessories.marker.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api      :as pretty-rules]
              [pretty-standards.api  :as pretty-standards]))

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
            (pretty-standards/standard-animation-props)
            (pretty-standards/standard-inner-position-props)
            (pretty-standards/standard-inner-size-props)
            (pretty-standards/standard-outer-position-props)
            (pretty-standards/standard-outer-size-props)
            (pretty-rules/auto-disable-mouse-events)))
