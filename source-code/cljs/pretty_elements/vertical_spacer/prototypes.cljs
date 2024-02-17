
(ns pretty-elements.vertical-spacer.prototypes
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
  (-> spacer-props (pretty-properties/default-size-props {:height :parent :width :s :size-unit :quarter-block})
                   (pretty-standards/standard-wrapper-size-props)
                   (pretty-rules/auto-adapt-wrapper-size)))
