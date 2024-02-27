
(ns pretty-elements.icon-button.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [react-references.api :as react-references]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]
              [pretty-subitems.api :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn icon-props-prototype
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ; @param (map) adornment-props
  ; {:icon (map)(opt)
  ;  ...}
  ;
  ; @return (map)
  [_ {:keys [icon]}]
  (-> icon (pretty-properties/default-icon-props {:icon-size :m})))

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
  (-> label (pretty-properties/default-font-props {:font-size :micro :font-weight :medium})
            (pretty-properties/default-text-props {:text-selectable? false})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  [button-id button-props]
  (let [set-reference-f         (fn [%] (react-references/set-reference! button-id %))
        icon-props-prototype-f  (fn [_] (icon-props-prototype            button-id button-props))
        label-props-prototype-f (fn [_] (label-props-prototype           button-id button-props))]
       (-> button-props (pretty-properties/default-flex-props       {:orientation :vertical})
                        (pretty-properties/default-outer-size-props {:outer-size-unit :full-block})
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
                        (pretty-subitems/apply-subitem-prototype :icon  icon-props-prototype-f)
                        (pretty-subitems/apply-subitem-prototype :label label-props-prototype-f))))
