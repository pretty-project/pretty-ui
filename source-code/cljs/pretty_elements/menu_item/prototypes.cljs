
(ns pretty-elements.menu-item.prototypes
    (:require [pretty-elements.menu-item.side-effects :as menu-item.side-effects]
              [pretty-properties.api                  :as pretty-properties]
              [pretty-rules.api                       :as pretty-rules]
              [pretty-standards.api                   :as pretty-standards]))

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
                 (pretty-standards/standard-anchor-props)
                 (pretty-standards/standard-border-props)
                 (pretty-standards/standard-flex-props)
                 (pretty-standards/standard-inner-position-props)
                 (pretty-standards/standard-inner-size-props)
                 (pretty-standards/standard-outer-position-props)
                 (pretty-standards/standard-outer-size-props)
                 (pretty-rules/apply-auto-border-crop)
                 (pretty-rules/auto-align-scrollable-flex)
                 (pretty-rules/auto-blur-click-events)
                 (pretty-rules/auto-disable-cursor)
                 (pretty-rules/auto-disable-mouse-events)
                 (pretty-rules/auto-set-mounted))))
