
(ns pretty-elements.label.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-props-prototype
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;
  ; @return (map)
  [label-id label-props]
  ; @bug (#9811)
  ; - The content can be an empty string before it gets its eventual value (e.g., from a subscription or a HTTP request, etc.).
  ;   An empty content placeholder and a delayed content can cause a short flickering due to inconsistent content height!
  ;   Therefore, the content placeholder must have at least a blank character as its content (e.g., "\u00A0").
  (-> label-props (pretty-properties/default-content-props {:content-placeholder "\u00A0"})
                  (pretty-properties/default-flex-props    {:orientation :horizontal})
                  (pretty-properties/default-font-props    {:font-size :s :font-weight :medium})
                  (pretty-properties/default-size-props    {:size-unit :full-block})
                  (pretty-properties/default-text-props    {:text-selectable? false})
                  (pretty-standards/standard-body-size-props)
                  (pretty-standards/standard-border-props)
                  (pretty-standards/standard-flex-props)
                  (pretty-standards/standard-font-props)
                  (pretty-standards/standard-icon-props)
                  (pretty-standards/standard-size-props)
                  (pretty-standards/standard-text-props)
                  (pretty-rules/apply-auto-border-crop)
                  (pretty-rules/auto-align-scrollable-flex)
                 ;(pretty-rules/auto-disable-highlight-color)
                 ;(pretty-rules/auto-disable-hover-color)
                  (pretty-rules/compose-content)
                  (pretty-rules/inherit-icon-props)))
