
(ns pretty-elements.expandable.prototypes
    (:require [pretty-properties.api                   :as pretty-properties]
              [pretty-standards.api                   :as pretty-standards]
              [pretty-rules.api                   :as pretty-rules]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expandable-props-prototype
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ;
  ; @return (map)
  [_ expandable-props]
  (-> expandable-props (pretty-properties/default-expandable-props {:expanded? true})
                       (pretty-properties/default-outer-size-props {:outer-size-unit :double-block})
                       (pretty-standards/standard-border-props)
                       (pretty-standards/standard-inner-position-props)
                       (pretty-standards/standard-inner-size-props)
                       (pretty-standards/standard-outer-position-props)
                       (pretty-standards/standard-outer-size-props)
                       (pretty-rules/apply-auto-border-crop)
                      ;(pretty-rules/auto-disable-highlight-color)
                      ;(pretty-rules/auto-disable-hover-color)
                       (pretty-rules/compose-content)))
