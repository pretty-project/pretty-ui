
(ns pretty-elements.menu-item.prototypes
    (:require [pretty-elements.menu-item.side-effects :as menu-item.side-effects]
              [pretty-properties.api                  :as pretty-properties]
              [pretty-models.api :as pretty-models]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn props-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  [id props]
  (let [on-mouse-over-f (fn [_] (menu-item.side-effects/on-mouse-over-f id props))]
       (-> props (pretty-properties/default-mouse-event-props {:on-mouse-over-f on-mouse-over-f})
                 (pretty-properties/default-flex-props        {:orientation :horizontal})
                 (pretty-properties/default-outer-size-props  {:outer-size-unit :full-block})
                 (pretty-models/click-control-standard-props)
                 (pretty-models/click-control-rules)
                 (pretty-models/flex-container-standard-props)
                 (pretty-models/flex-container-rules))))
