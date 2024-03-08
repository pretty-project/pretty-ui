
(ns pretty-elements.adornment.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api      :as pretty-rules]
              [pretty-standards.api  :as pretty-standards]
              [pretty-subitems.api   :as pretty-subitems]
              [react-references.api  :as react-references]))

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
  [_ _ label]
  (-> label (pretty-properties/default-font-props {:font-size :xxs :font-weight :medium})))

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
  (let [set-reference-f   (fn [%] (react-references/set-reference! id       %))
        label-prototype-f (fn [%] (label-prototype                 id props %))]
       (-> props (pretty-properties/default-flex-props       {:orientation :horizontal})
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
                 (pretty-rules/auto-set-mounted)
                 (pretty-subitems/apply-subitem-longhand  :label :content)
                 (pretty-subitems/apply-subitem-prototype :label label-prototype-f))))
