
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
  (-> badge-props (pretty-properties/default-font-props           {:font-size :micro :font-weight :medium})
                  (pretty-properties/default-outer-position-props {:outer-position :br :outer-position-method :absolute})
                  (pretty-properties/default-outer-size-props     {:outer-size-unit :quarter-block})
                  (pretty-properties/default-text-props           {:text-selectable? false})
                  (pretty-standards/standard-animation-props)
                  (pretty-standards/standard-border-props)
                  (pretty-standards/standard-font-props)
                  (pretty-standards/standard-icon-props)
                  (pretty-standards/standard-inner-position-props)
                  (pretty-standards/standard-inner-size-props)
                  (pretty-standards/standard-outer-position-props)
                  (pretty-standards/standard-outer-size-props)
                  (pretty-standards/standard-text-props)
                  (pretty-rules/apply-auto-border-crop)
                 ;(pretty-rules/auto-disable-highlight-color)
                 ;(pretty-rules/auto-disable-hover-color)
                  (pretty-rules/compose-label)))
