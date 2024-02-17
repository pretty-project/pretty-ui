
(ns pretty-accessories.badge.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn badge-props-prototype
  ; @ignore
  ;
  ; @param (keyword) badge-id
  ; @param (map) badge-props
  ;
  ; @return (map)
  [_ badge-props]
  (-> badge-props (pretty-properties/default-font-props     {:font-size :micro :font-weight :medium})
                  (pretty-properties/default-position-props {:position :br :position-method :absolute})
                  (pretty-properties/default-size-props     {:size-unit :quarter-block})
                  (pretty-properties/default-text-props     {:text-selectable? false})
                  (pretty-standards/standard-border-props)
                  (pretty-standards/standard-font-props)
                  (pretty-standards/standard-icon-props)
                  (pretty-standards/standard-text-props)
                  (pretty-standards/standard-wrapper-size-props)
                  ;(pretty-rules/auto-disable-highlight-color)
                  ;(pretty-rules/auto-disable-hover-color)
                  (pretty-rules/apply-auto-border-crop)
                  (pretty-rules/auto-adapt-wrapper-size)
                  (pretty-rules/compose-label)))
