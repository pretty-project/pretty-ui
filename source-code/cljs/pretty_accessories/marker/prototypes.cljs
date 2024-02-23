
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
                   (pretty-properties/default-position-props         {:position :tr :position-method :absolute})
                   (pretty-properties/default-size-props             {:height :xxs :width :xxs :size-unit :quarter-block})
                   (pretty-standards/standard-body-size-props)
                   (pretty-standards/standard-size-props)))
                  ;(pretty-rules/auto-disable-highlight-color)
                  ;(pretty-rules/auto-disable-hover-color)
