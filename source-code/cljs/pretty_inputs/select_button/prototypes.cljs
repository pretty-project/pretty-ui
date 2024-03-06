
(ns pretty-inputs.select-button.prototypes
    (:require [pretty-inputs.engine.api :as pretty-inputs.engine]
              [pretty-properties.api    :as pretty-properties]
              [pretty-rules.api         :as pretty-rules]
              [pretty-standards.api     :as pretty-standards]
              [pretty-subitems.api      :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn icon-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; @param (map) icon
  ;
  ; @return (map)
  [_ _ icon]
  (-> icon (pretty-properties/default-icon-props {:icon-name :unfold_more})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; @param (map) label
  ;
  ; @return (map)
  [id props label]
  (if-let [external-value (pretty-inputs.engine/get-input-external-value id props)]
          (-> label (assoc :content (str external-value)))
          (-> label)))

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
  (let [icon-prototype-f  (fn [%] (icon-prototype  id props %))
        label-prototype-f (fn [%] (label-prototype id props %))]
       (-> props (pretty-properties/default-flex-props       {:gap :auto :orientation :horizontal})
                 (pretty-properties/default-outer-size-props {:outer-size-unit :full-block})
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
                 (pretty-rules/auto-set-mounted)
                 (pretty-subitems/ensure-subitem          :icon :label)
                 (pretty-subitems/apply-subitem-prototype :icon  icon-prototype-f)
                 (pretty-subitems/apply-subitem-prototype :label label-prototype-f))))
