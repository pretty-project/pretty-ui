
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
  (-> cover-props (pretty-properties/default-background-color-props {:fill-color :default})
                  (pretty-properties/default-flex-props             {:orientation :horizontal})
                  (pretty-properties/default-font-props             {:font-size :s :font-weight :medium})
                  (pretty-properties/default-position-props         {:position :tl :position-method :absolute})
                  (pretty-properties/default-size-props             {:height :parent :width :parent :size-unit :quarter-block})
                  (pretty-properties/default-text-props             {:text-selectable? false})
                  (pretty-properties/default-visibility-props       {:opacity :hard})
                  (pretty-standards/standard-flex-props)
                  (pretty-standards/standard-font-props)
                  (pretty-standards/standard-icon-props)
                  (pretty-standards/standard-text-props)
                  (pretty-standards/standard-wrapper-size-props)
                  (pretty-rules/auto-adapt-wrapper-size)
                 ;(pretty-rules/auto-disable-highlight-color)
                 ;(pretty-rules/auto-disable-hover-color)
                  (pretty-rules/compose-label)))
