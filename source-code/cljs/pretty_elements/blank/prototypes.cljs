
(ns pretty-elements.blank.prototypes
    (:require [pretty-models.api :as pretty-models]))

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
  (-> props (pretty-models/flex-container-standard-props)
            (pretty-models/flex-container-rules)
            (pretty-models/plain-content-standard-props)
            (pretty-models/plain-content-rules)))
