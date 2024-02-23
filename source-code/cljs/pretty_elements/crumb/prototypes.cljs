
(ns pretty-elements.crumb.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn crumb-props-prototype
  ; @ignore
  ;
  ; @param (keyword) crumb-id
  ; @param (map) crumb-props
  ;
  ; @return (map)
  [_ crumb-props]
  (-> crumb-props (pretty-properties/default-flex-props {:orientation :horizontal})
                  (pretty-properties/default-font-props {:font-size :xs :font-weight :semi-bold})
                  (pretty-properties/default-size-props {:max-width :l :size-unit :full-block})
                  (pretty-properties/default-text-props {:text-overflow :ellipsis :text-selectable? false})
                  (pretty-standards/standard-anchor-props)
                  (pretty-standards/standard-body-size-props)
                  (pretty-standards/standard-flex-props)
                  (pretty-standards/standard-font-props)
                  (pretty-standards/standard-size-props)
                  (pretty-standards/standard-text-props)
                  (pretty-rules/auto-align-scrollable-flex)
                  (pretty-rules/auto-blur-click-events)
                  (pretty-rules/auto-color-clickable-text)
                  (pretty-rules/auto-disable-highlight-color)
                  (pretty-rules/auto-disable-effects)
                  (pretty-rules/auto-disable-hover-color)
                  (pretty-rules/auto-disable-mouse-events)
                  (pretty-rules/auto-set-click-effect)
                  (pretty-rules/compose-label)))
