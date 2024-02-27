
(ns pretty-elements.chip.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]
              [pretty-subitems.api :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-props-prototype
  ; @ignore
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ; {:label (map)(opt)
  ;  ...}
  ;
  ; @return (map)
  [_ {:keys [label]}]
  (-> label (pretty-properties/default-font-props {:font-size :xxs :font-weight :medium})
            (pretty-properties/default-text-props {:text-overflow :ellipsis :text-selectable? false})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-props-prototype
  ; @ignore
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ;
  ; @return (map)
  [chip-id chip-props]
  (let [label-props-prototype-f (fn [_] (label-props-prototype chip-id chip-props))]
       (-> chip-props (pretty-properties/default-background-color-props {:fill-color :primary})
                      (pretty-properties/default-outer-size-props       {:outer-size-unit :full-block})
                      (pretty-standards/standard-anchor-props)
                      (pretty-standards/standard-border-props)
                      (pretty-standards/standard-inner-position-props)
                      (pretty-standards/standard-inner-size-props)
                      (pretty-standards/standard-outer-position-props)
                      (pretty-standards/standard-outer-size-props)
                      (pretty-rules/apply-auto-border-crop)
                      (pretty-rules/auto-blur-click-events)
                      (pretty-rules/auto-disable-cursor)
                      (pretty-rules/auto-disable-effects)
                      (pretty-rules/auto-disable-highlight-color)
                      (pretty-rules/auto-disable-hover-color)
                      (pretty-rules/auto-disable-mouse-events)
                      (pretty-rules/auto-set-click-effect)
                      (pretty-subitems/apply-subitem-prototype :label label-props-prototype-f))))
