
(ns pretty-elements.button.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-models.api  :as pretty-models]))

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
            (pretty-properties/default-outer-size-props {:outer-size-unit :full-block})
            (pretty-models/click-control-standard-props)
            (pretty-models/click-control-rules)
            (pretty-models/flex-container-standard-props)
            (pretty-models/flex-container-rules)))
