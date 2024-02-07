
(ns pretty-elements.horizontal-spacer.prototypes
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
  (-> spacer-props (pretty-properties/default-size-props {:height :s :width :auto})))
