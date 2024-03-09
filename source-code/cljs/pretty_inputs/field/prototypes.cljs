
(ns pretty-inputs.field.prototypes
    (:require [dom.api                  :as dom]
              [pretty-inputs.engine.api :as pretty-inputs.engine]
              [pretty-properties.api    :as pretty-properties]
              [pretty-rules.api         :as pretty-rules]
              [pretty-standards.api     :as pretty-standards]
              [pretty-subitems.api      :as pretty-subitems]
              [react-references.api     :as react-references]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn end-adornment-group-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; @param (map) end-adornment-group
  ;
  ; @return (map)
  [id props end-adornment-group]
  (let [on-mouse-down-f (fn [e] (dom/prevent-default e))
        on-mouse-up-f   (fn [_] (pretty-inputs.engine/focus-input! id props))]
       (-> end-adornment-group (pretty-properties/default-mouse-event-props {:on-mouse-down-f on-mouse-down-f :on-mouse-up-f on-mouse-up-f}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn start-adornment-group-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; @param (map) start-adornment-group
  ;
  ; @return (map)
  [id props start-adornment-group]
  (let [on-mouse-down-f (fn [e] (dom/prevent-default e))
        on-mouse-up-f   (fn [_] (pretty-inputs.engine/focus-input! id props))]
       (-> start-adornment-group (pretty-properties/default-mouse-event-props {:on-mouse-down-f on-mouse-down-f :on-mouse-up-f on-mouse-up-f}))))

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
  (let [on-mouse-down-f (fn [e] (dom/prevent-default e))
        on-mouse-up-f   (fn [_] (pretty-inputs.engine/focus-input! id props))]
       (if "(pretty-inputs.engine/input-focused? id props)"
           (-> expandable (pretty-properties/default-mouse-event-props    {:on-mouse-down-f on-mouse-down-f :on-mouse-up-f on-mouse-up-f})
                          (pretty-properties/default-outer-position-props {:outer-position :bottom :outer-position-base :external :outer-position-method :absolute})
                          (pretty-properties/default-outer-size-props     {:outer-width :parent})))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn placeholder-text-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:field-value (string)(opt)
  ;  ...}
  ; @param (map) placeholder-text
  ;
  ; @return (map)
  [_ {:keys [field-value]} placeholder-text]
  (if (-> field-value empty?)
      (-> placeholder-text (pretty-properties/default-outer-position-props {:outer-position-method :absolute}))))

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
  (let [set-reference-f                   (fn [%] (react-references/set-reference! id %))
        expandable-prototype-f            (fn [%] (expandable-prototype            id props %))
        placeholder-text-prototype-f      (fn [%] (placeholder-text-prototype      id props %))
        end-adornment-group-prototype-f   (fn [%] (end-adornment-group-prototype   id props %))
        start-adornment-group-prototype-f (fn [%] (start-adornment-group-prototype id props %))]
       (-> props (pretty-properties/default-font-props        {:font-size :s :font-weight :normal})
                 (pretty-properties/default-flex-props        {:orientation :horizontal})
                 (pretty-properties/default-input-field-props {:field-type :text})
                 (pretty-properties/default-react-props       {:set-reference-f set-reference-f})
                 (pretty-properties/default-outer-size-props  {:outer-size-unit :full-block})
                 (pretty-standards/standard-border-props)
                 (pretty-standards/standard-font-props)
                 (pretty-standards/standard-flex-props)
                 (pretty-standards/standard-inner-position-props)
                 (pretty-standards/standard-inner-size-props)
                 (pretty-standards/standard-outer-position-props)
                 (pretty-standards/standard-outer-size-props)
                 (pretty-standards/standard-text-props)
                ;(pretty-rules/apply-auto-border-crop)
                ;(pretty-rules/auto-align-scrollable-flex)
                 (pretty-rules/assoc-field-value-as-content)
                 (pretty-rules/auto-count-content-lines)
                 (pretty-rules/auto-limit-multiline-count)
                 (pretty-rules/auto-set-multiline-height)
                 (pretty-rules/auto-disable-input-autofill)
                 (pretty-rules/auto-disable-input-events)
                 (pretty-rules/auto-disable-mouse-events)
                 (pretty-rules/generate-input-autofill)
                 (pretty-rules/auto-set-mounted)
                 (pretty-subitems/apply-subitem-prototype :expandable            expandable-prototype-f)
                 (pretty-subitems/apply-subitem-prototype :placeholder-text      placeholder-text-prototype-f)
                 (pretty-subitems/apply-subitem-prototype :end-adornment-group   end-adornment-group-prototype-f)
                 (pretty-subitems/apply-subitem-prototype :start-adornment-group start-adornment-group-prototype-f))))
