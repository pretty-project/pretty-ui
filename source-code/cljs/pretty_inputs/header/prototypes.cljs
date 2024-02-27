
(ns pretty-inputs.header.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]
              [fruits.vector.api :as vector]
              [pretty-inputs.engine.api :as pretty-inputs.engine]
              [pretty-inputs.header.adornments :as header.adornments]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-props-prototype
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ; {:label (map)(opt)
  ;  ...}
  ;
  ; @return (map)
  [header-id {:keys [label] :as header-props}]
  (let [toggle-info-text-adornment (header.adornments/toggle-info-text-adornment header-id header-props)]
       (-> label (update :end-adornments vector/conj-item toggle-info-text-adornment))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-props-prototype
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;
  ; @return (map)
  [header-id header-props]
  (let [on-mouse-up-f (fn [_] (pretty-inputs.engine/focus-input! header-id header-props))]
       (-> header-props (pretty-properties/default-flex-props        {:horizontal-align :left :orientation :vertical})
                        (pretty-properties/default-mouse-event-props {:on-mouse-up-f on-mouse-up-f})
                        (pretty-properties/default-outer-size-props  {:outer-size-unit :full-block})
                        (pretty-standards/standard-flex-props)
                        (pretty-standards/standard-inner-position-props)
                        (pretty-standards/standard-inner-size-props)
                        (pretty-standards/standard-outer-position-props)
                        (pretty-standards/standard-outer-size-props)
                        (pretty-rules/auto-align-scrollable-flex))))
                       ;(pretty-rules/auto-disable-highlight-color)
                       ;(pretty-rules/auto-disable-hover-color)
