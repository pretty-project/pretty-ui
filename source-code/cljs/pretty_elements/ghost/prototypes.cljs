
(ns pretty-elements.ghost.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost-props-prototype
  ; @ignore
  ;
  ; @param (keyword) ghost-id
  ; @param (map) ghost-props
  ;
  ; @return (map)
  [_ ghost-props]
  (-> ghost-props (pretty-properties/default-animation-props        {:animation-duration 2000 :animation-mode :repeat :animation-name :pulse})
                  (pretty-properties/default-background-color-props {:fill-color :highlight})
                  (pretty-properties/default-outer-size-props       {:outer-height :s :outer-width :s :outer-size-unit :full-block})
                  (pretty-standards/standard-animation-props)
                  (pretty-standards/standard-border-props)
                  (pretty-standards/standard-inner-position-props)
                  (pretty-standards/standard-inner-size-props)
                  (pretty-standards/standard-outer-position-props)
                  (pretty-standards/standard-outer-size-props)
                  (pretty-rules/apply-auto-border-crop)))
                 ;(pretty-rules/auto-disable-highlight-color)
                 ;(pretty-rules/auto-disable-hover-color)
