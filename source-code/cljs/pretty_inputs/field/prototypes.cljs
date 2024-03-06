
(ns pretty-inputs.field.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api      :as pretty-rules]
              [pretty-standards.api  :as pretty-standards]
              [pretty-subitems.api  :as pretty-subitems]
              [pretty-inputs.engine.api  :as pretty-inputs.engine]
              [react-references.api  :as react-references]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expandable-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; @param (map) expandable
  ;
  ; @return (map)
  [id props expandable]
  (if (pretty-inputs.engine/input-focused? id props)
      (-> expandable (pretty-properties/default-outer-position-props {:outer-position :bottom :outer-position-base :external :outer-position-method :absolute})
                     (pretty-properties/default-outer-size-props     {:outer-width :parent}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn placeholder-text-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:value (*)(opt)
  ;  ...}
  ; @param (map) placeholder-text
  ;
  ; @return (map)
  [_ {:keys [value] :as props} placeholder-text]
  (if-not value (-> placeholder-text (pretty-properties/default-outer-position-props {:outer-position-method :absolute}))))

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
  (let [set-reference-f              (fn [%] (react-references/set-reference!   id %))
        focus-input-f                (fn [_] (pretty-inputs.engine/focus-input! id props))
        expandable-prototype-f       (fn [%] (expandable-prototype              id props %))
        placeholder-text-prototype-f (fn [%] (placeholder-text-prototype        id props %))]
       (-> props (pretty-properties/default-flex-props        {:orientation :horizontal})
                 (pretty-properties/default-input-field-props {:field-type :text})
                 (pretty-properties/default-mouse-event-props {:on-mouse-up-f focus-input-f})
                 (pretty-properties/default-react-props       {:set-reference-f set-reference-f})
                 ;(pretty-properties/default-outer-size-props {:outer-size-unit :full-block})
                 ;(pretty-standards/standard-border-props)
                 ;(pretty-standards/standard-flex-props)
                 (pretty-standards/standard-inner-position-props)
                 (pretty-standards/standard-inner-size-props)
                 (pretty-standards/standard-outer-position-props)
                 (pretty-standards/standard-outer-size-props)
                 ;(pretty-rules/apply-auto-border-crop)
                 (pretty-rules/auto-disable-input-events)
                ;(pretty-rules/auto-align-scrollable-flex)
                 ;(pretty-rules/auto-blur-click-events)
                 ;(pretty-rules/auto-disable-cursor)
                 ;(pretty-rules/auto-disable-effects)
                 ;(pretty-rules/auto-disable-highlight-color)
                 ;(pretty-rules/auto-disable-hover-color)
                 ;(pretty-rules/auto-disable-mouse-events)
                 ;(pretty-rules/auto-set-click-effect)
                 (pretty-rules/auto-set-mounted)
                 (pretty-subitems/apply-subitem-prototype :expandable       expandable-prototype-f)
                 (pretty-subitems/apply-subitem-prototype :placeholder-text placeholder-text-prototype-f))))
