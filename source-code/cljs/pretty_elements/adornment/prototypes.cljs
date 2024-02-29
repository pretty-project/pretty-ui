
(ns pretty-elements.adornment.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [react-references.api :as react-references]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]
              [pretty-subitems.api :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-props-prototype
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ; @param (map) adornment-props
  ; {:label (map)(opt)
  ;  ...}
  ;
  ; @return (map)
  [_ {:keys [label]}]
  (-> label (pretty-properties/default-font-props {:font-size :xxs :font-weight :medium})
            (pretty-properties/default-text-props {:text-selectable? false})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adornment-props-prototype
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ; @param (map) adornment-props
  ;
  ; @return (map)
  [adornment-id adornment-props]
  (let [set-reference-f         (fn [%] (react-references/set-reference! adornment-id %))
        label-props-prototype-f (fn [_] (label-props-prototype           adornment-id adornment-props))]
       (-> adornment-props (pretty-properties/default-flex-props       {:orientation :horizontal})
                           (pretty-properties/default-outer-size-props {:min-outer-width :xs :outer-size-unit :half-block})
                           (pretty-properties/default-react-props      {:set-reference-f set-reference-f})
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
                           (pretty-rules/auto-disable-effects)
                           (pretty-rules/auto-disable-highlight-color)
                           (pretty-rules/auto-disable-hover-color)
                           (pretty-rules/auto-disable-mouse-events)
                           (pretty-rules/auto-set-click-effect)
                           (pretty-subitems/apply-subitem-prototype :label label-props-prototype-f))))
