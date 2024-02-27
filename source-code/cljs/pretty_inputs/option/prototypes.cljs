
(ns pretty-inputs.checkbox.prototypes
    (:require [pretty-standards.api :as pretty-standards]
              [pretty-rules.api :as pretty-rules]
              [pretty-properties.api :as pretty-properties]
              [pretty-inputs.engine.api :as pretty-inputs.engine]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn checkbox-props-prototype
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ;
  ; @return (map)
  [checkbox-id checkbox-props]
  (let [on-blur-f  (fn [_] (pretty-inputs.engine/input-left    checkbox-id checkbox-props))
        on-focus-f (fn [_] (pretty-inputs.engine/input-focused checkbox-id checkbox-props))]
       (-> checkbox-props (pretty-properties/default-border-props      {:border-color :default :border-width :xs})
                          (pretty-properties/default-effect-props      {:click-effect :opacity})
                          (pretty-properties/default-flex-props        {:orientation :vertical :horizontal-align :left})
                          (pretty-properties/default-focus-event-props {:on-blur-f on-blur-f :on-focus-f on-focus-f})
                          (pretty-properties/default-font-props        {:font-size :s :font-weight :medium})
                          (pretty-properties/default-outer-size-props  {:outer-size-unit :full-block})
                          (pretty-properties/default-text-props        {:text-overflow :ellipsis :text-selectable? false})
                          (pretty-standards/standard-border-props)
                          (pretty-standards/standard-flex-props)
                          (pretty-standards/standard-font-props)
                          (pretty-standards/standard-inner-position-props)
                          (pretty-standards/standard-inner-size-props)
                          (pretty-standards/standard-input-option-props)
                          (pretty-standards/standard-outer-position-props)
                          (pretty-standards/standard-outer-size-props)
                          (pretty-standards/standard-text-props)
                         ;(pretty-rules/apply-auto-border-crop)
                          (pretty-rules/auto-align-scrollable-flex)
                          (pretty-rules/auto-blur-click-events)
                          (pretty-rules/auto-disable-cursor)
                          (pretty-rules/auto-disable-effects)
                          (pretty-rules/auto-disable-mouse-events))))
