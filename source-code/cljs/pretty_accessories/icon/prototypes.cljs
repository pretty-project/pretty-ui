
(ns pretty-accessories.icon.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-standards.api  :as pretty-standards]
              [pretty-rules.api  :as pretty-rules]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn props-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  [_ props]
  (-> props (pretty-properties/default-flex-props       {:orientation :horizontal})
            (pretty-properties/default-icon-props       {:icon-size :s})
            (pretty-properties/default-outer-size-props {:outer-size-unit :half-block})
            (pretty-standards/standard-border-props)
            (pretty-standards/standard-flex-props)
            (pretty-standards/standard-icon-props)
            (pretty-standards/standard-inner-position-props)
            (pretty-standards/standard-inner-size-props)
            (pretty-standards/standard-outer-position-props)
            (pretty-standards/standard-outer-size-props)
           ;(pretty-rules/apply-auto-border-crop)
            (pretty-rules/auto-disable-mouse-events)))
