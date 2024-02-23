
(ns pretty-accessories.marker.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn marker-props-prototype
  ; @ignore
  ;
  ; @param (keyword) marker-id
  ; @param (map) marker-props
  ;
  ; @return (map)
  [_ marker-props]
  (-> marker-props (pretty-properties/default-background-color-props {:fill-color :default})
                   (pretty-properties/default-outer-size-props       {:outer-height :xxs :outer-width :xxs :outer-size-unit :quarter-block})
                   (pretty-properties/default-position-props         {:position :tr :position-method :absolute})
                   (pretty-standards/standard-inner-size-props)
                   (pretty-standards/standard-outer-size-props)))
                  ;(pretty-rules/auto-disable-highlight-color)
                  ;(pretty-rules/auto-disable-hover-color)
