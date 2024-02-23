
(ns pretty-elements.horizontal-spacer.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn spacer-props-prototype
  ; @ignore
  ;
  ; @param (keyword) spacer-id
  ; @param (map) spacer-props
  ;
  ; @return (map)
  [_ spacer-props]
  (-> spacer-props (pretty-properties/default-flex-props {:shrink 0})
                   (pretty-properties/default-size-props {:height :s :width :auto :size-unit :quarter-block})
                   (pretty-standards/standard-body-size-props)
                   (pretty-standards/standard-size-props)))
