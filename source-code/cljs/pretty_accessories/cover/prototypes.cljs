
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
  (-> cover-props (pretty-properties/default-flex-props     {:orientation :horizontal})
                  (pretty-properties/default-font-props     {:font-size :s :font-weight :medium})
                  (pretty-properties/default-overlay-props  {:overlay-color :invert :overlay-opacity :hard})
                  (pretty-properties/default-position-props {:position :tl :position-method :absolute})
                  (pretty-properties/default-size-props     {:height :parent :width :parent :size-unit :quarter-block})
                  (pretty-properties/default-text-props     {:text-selectable? false})
                  (pretty-standards/standard-body-size-props)
                  (pretty-standards/standard-flex-props)
                  (pretty-standards/standard-font-props)
                  (pretty-standards/standard-icon-props)
                  (pretty-standards/standard-overlay-props)
                  (pretty-standards/standard-size-props)
                  (pretty-standards/standard-text-props)
                  (pretty-rules/compose-label)))
