
(ns pretty-elements.vertical-spacer.prototypes
    (:require [pretty-properties.api :as pretty-properties]))

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
  (-> spacer-props (pretty-properties/default-size-props         {:height :parent :width :s :size-unit :quarter-block})
                   (pretty-properties/default-wrapper-size-props {})))
