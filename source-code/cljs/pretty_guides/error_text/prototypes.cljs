
(ns pretty-guides.error-text.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn text-props-prototype
  ; @ignore
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;
  ; @return (map)
  [_ text-props]
  (-> text-props (pretty-properties/default-font-props       {:font-size :xs :font-weight :normal})
                 (pretty-properties/default-outer-size-props {:outer-size-unit :quarter-block})
                 (pretty-properties/default-text-props       {:text-color :warning :text-selectable? true})
                 (pretty-standards/standard-font-props)
                 (pretty-standards/standard-inner-position-props)
                 (pretty-standards/standard-inner-size-props)
                 (pretty-standards/standard-outer-position-props)
                 (pretty-standards/standard-outer-size-props)
                 (pretty-standards/standard-text-props)
                 (pretty-rules/compose-content)))
