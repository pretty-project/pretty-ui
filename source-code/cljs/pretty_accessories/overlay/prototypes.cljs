
(ns pretty-accessories.overlay.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-standards.api :as pretty-standards]
              [pretty-rules.api :as pretty-rules]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn overlay-props-prototype
  ; @ignore
  ;
  ; @param (keyword) overlay-id
  ; @param (map) overlay-props
  ;
  ; @return (map)
  [_ overlay-props]
  (-> overlay-props (pretty-properties/default-background-color-props {:fill-color :invert})
                    (pretty-properties/default-outer-position-props   {:outer-position :tl :outer-position-method :absolute})
                    (pretty-properties/default-outer-size-props       {:outer-height :parent :outer-width :parent :outer-size-unit :full-block})
                    (pretty-properties/default-visibility-props       {:opacity :medium})
                    (pretty-standards/standard-animation-props)
                    (pretty-standards/standard-inner-position-props)
                    (pretty-standards/standard-inner-size-props)
                    (pretty-standards/standard-outer-position-props)
                    (pretty-standards/standard-outer-size-props)
                    (pretty-rules/auto-blur-click-events)
                   ;(pretty-rules/auto-disable-highlight-color)
                   ;(pretty-rules/auto-disable-hover-color
                    (pretty-rules/auto-disable-mouse-events)))