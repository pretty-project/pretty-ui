
(ns pretty-accessories.bullet.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bullet-props-prototype
  ; @ignore
  ;
  ; @param (keyword) bullet-id
  ; @param (map) bullet-props
  ;
  ; @return (map)
  [_ bullet-props]
  (-> bullet-props (pretty-properties/default-background-color-props {:fill-color :muted})
                   (pretty-properties/default-outer-size-props       {:outer-height :xxs :outer-width :xxs :outer-size-unit :quarter-block})
                   (pretty-standards/standard-inner-position-props)
                   (pretty-standards/standard-inner-size-props)
                   (pretty-standards/standard-outer-position-props)
                   (pretty-standards/standard-outer-size-props)))
                  ;(pretty-rules/auto-disable-highlight-color)
                  ;(pretty-rules/auto-disable-hover-color)
