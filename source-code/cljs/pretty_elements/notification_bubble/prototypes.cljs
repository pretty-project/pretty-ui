
(ns pretty-elements.notification-bubble.prototypes
    (:require [pretty-models.api     :as pretty-models]
              [pretty-properties.api :as pretty-properties]
              [pretty-subitems.api   :as pretty-subitems]))

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
            (pretty-models/flex-container-standard-props)
            (pretty-models/flex-container-rules)
            (pretty-models/plain-content-standard-props)
            (pretty-models/plain-content-rules)
            (pretty-subitems/subitems<-disabled-state :start-adornment-group :end-adornment-group)))
