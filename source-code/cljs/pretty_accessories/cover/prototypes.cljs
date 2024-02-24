
(ns pretty-accessories.cover.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn cover-props-prototype
  ; @ignore
  ;
  ; @param (keyword) cover-id
  ; @param (map) cover-props
  ;
  ; @return (map)
  [_ cover-props]
  (-> cover-props (pretty-properties/default-background-color-props {:fill-color :invert})
                  (pretty-properties/default-flex-props             {:orientation :horizontal})
                  (pretty-properties/default-font-props             {:font-size :s :font-weight :semi-bold})
                  (pretty-properties/default-outer-position-props   {:outer-position :tl :outer-position-method :absolute})
                  (pretty-properties/default-outer-size-props       {:outer-height :parent :outer-width :parent :outer-size-unit :quarter-block})
                  (pretty-properties/default-text-props             {:text-color :invert :text-selectable? false})
                  (pretty-properties/default-visibility-props       {:opacity :hard})
                  (pretty-standards/standard-animation-props)
                  (pretty-standards/standard-flex-props)
                  (pretty-standards/standard-font-props)
                  (pretty-standards/standard-icon-props)
                  (pretty-standards/standard-inner-position-props)
                  (pretty-standards/standard-inner-size-props)
                  (pretty-standards/standard-outer-position-props)
                  (pretty-standards/standard-outer-size-props)
                  (pretty-standards/standard-text-props)
                 ;(pretty-rules/auto-disable-highlight-color)
                 ;(pretty-rules/auto-disable-hover-color)
                  (pretty-rules/compose-label)))
