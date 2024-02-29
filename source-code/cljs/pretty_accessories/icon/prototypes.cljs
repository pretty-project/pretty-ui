
(ns pretty-accessories.icon.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-standards.api :as pretty-standards]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn icon-props-prototype
  ; @ignore
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  ;
  ; @return (map)
  [_ icon-props]
  (-> icon-props (pretty-properties/default-flex-props       {:orientation :horizontal})
                 (pretty-properties/default-icon-props       {:icon-size :m})
                 (pretty-properties/default-outer-size-props {:outer-size-unit :half-block})
                 (pretty-standards/standard-border-props)
                 (pretty-standards/standard-flex-props)
                 (pretty-standards/standard-icon-props)
                 (pretty-standards/standard-inner-position-props)
                 (pretty-standards/standard-inner-size-props)
                 (pretty-standards/standard-outer-position-props)
                 (pretty-standards/standard-outer-size-props)))
                ;(pretty-rules/apply-auto-border-crop)
                ;(pretty-rules/auto-disable-highlight-color)
                ;(pretty-rules/auto-disable-hover-color)
