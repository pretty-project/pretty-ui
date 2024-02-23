
(ns pretty-layouts.body.prototypes
    (:require [pretty-rules.api :as pretty-rules]
              [pretty-properties.api :as pretty-properties]
              [pretty-standards.api :as pretty-standards]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-props-prototype
  ; @ignore
  ;
  ; @param (keyword) body-id
  ; @param (map) body-props
  ;
  ; @return (map)
  [_ body-props]
  (-> body-props (pretty-properties/default-flex-props       {:orientation :horizontal})
                 (pretty-properties/default-outer-size-props {:outer-height :content :outer-width :parent :outer-size-unit :half-block})
                 (pretty-standards/standard-border-props)
                 (pretty-standards/standard-flex-props)
                 (pretty-standards/standard-inner-size-props)
                 (pretty-standards/standard-outer-size-props)
                 (pretty-rules/apply-auto-border-crop)
                 (pretty-rules/auto-align-scrollable-flex)
                ;(pretty-rules/auto-disable-highlight-color)
                ;(pretty-rules/auto-disable-hover-color
                 (pretty-rules/compose-content)))
