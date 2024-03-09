
(ns pretty-accessories.overlay.prototypes
    (:require [pretty-models.api     :as pretty-models]
              [pretty-properties.api :as pretty-properties]))

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
  (-> props (pretty-properties/default-background-color-props {:fill-color :default})
            (pretty-properties/default-outer-position-props   {:outer-position :tl :outer-position-method :absolute})
            (pretty-properties/default-outer-size-props       {:outer-height :parent :outer-width :parent :outer-size-unit :full-block})
            (pretty-properties/default-visibility-props       {:opacity :medium})
            (pretty-models/plain-model-standard-props)
            (pretty-models/plain-model-rules)))
