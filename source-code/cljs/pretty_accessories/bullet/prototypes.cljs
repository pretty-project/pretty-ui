
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
                   (pretty-properties/default-size-props             {:height :xxs :width :xxs :size-unit :quarter-block})
                   (pretty-standards/standard-wrapper-size-props)
                   ;(pretty-rules/auto-disable-highlight-color)
                   ;(pretty-rules/auto-disable-hover-color)
                   (pretty-rules/auto-adapt-wrapper-size)))
