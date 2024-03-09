
(ns pretty-elements.notification-bubble.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-models.api :as pretty-models]
              [pretty-subitems.api :as pretty-subitems]))

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
  (-> props (pretty-properties/default-font-props       {:font-size :xxs :font-weight :medium})
            (pretty-properties/default-flex-props       {:orientation :horizontal :horizontal-align :left :gap :auto})
            (pretty-properties/default-outer-size-props {:outer-size-unit :double-block})
            (pretty-properties/default-text-props       {:text-overflow :wrap})
            (pretty-models/container-model-standard-props)
            (pretty-models/container-model-rules)
            (pretty-models/content-model-standard-props)
            (pretty-models/content-model-rules)
            (pretty-subitems/subitems<-disabled-state :start-adornment-group :end-adornment-group)))
