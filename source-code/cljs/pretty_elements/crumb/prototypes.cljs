
(ns pretty-elements.crumb.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]
              [pretty-subitems.api :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-props-prototype
  ; @ignore
  ;
  ; @param (keyword) crumb-id
  ; @param (map) crumb-props
  ; {:label (map)(opt)
  ;  ...}
  ;
  ; @return (map)
  [_ {:keys [label]}]
  (-> label (pretty-properties/default-font-props {:font-size :xs :font-weight :semi-bold})
            (pretty-properties/default-text-props {:text-overflow :ellipsis :text-selectable? false})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn crumb-props-prototype
  ; @ignore
  ;
  ; @param (keyword) crumb-id
  ; @param (map) crumb-props
  ;
  ; @return (map)
  [crumb-id crumb-props]
  (let [label-props-prototype-f (fn [_] (label-props-prototype crumb-id crumb-props))]
       (-> crumb-props (pretty-properties/default-flex-props       {:gap :xs :orientation :horizontal})
                       (pretty-properties/default-outer-size-props {:max-width :l :outer-size-unit :full-block})
                       (pretty-standards/standard-anchor-props)
                       (pretty-standards/standard-flex-props)
                       (pretty-standards/standard-inner-position-props)
                       (pretty-standards/standard-inner-size-props)
                       (pretty-standards/standard-outer-position-props)
                       (pretty-standards/standard-outer-size-props)
                       (pretty-rules/auto-align-scrollable-flex)
                       (pretty-rules/auto-blur-click-events)
                       (pretty-rules/auto-disable-highlight-color)
                       (pretty-rules/auto-disable-effects)
                       (pretty-rules/auto-disable-hover-color)
                       (pretty-rules/auto-disable-mouse-events)
                       (pretty-rules/auto-set-click-effect)
                       (pretty-subitems/apply-subitem-prototype :label label-props-prototype-f))))
