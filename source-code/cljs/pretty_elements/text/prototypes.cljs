
(ns pretty-elements.text.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn text-props-prototype
  ; @ignore
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;
  ; @return (map)
  [_ text-props]
  ; @bug (pretty-accessories.label.prototypes#9811)
  (-> text-props (pretty-properties/default-content-props    {:content-placeholder "\u00A0"})
                 (pretty-properties/default-flex-props       {:orientation :horizontal})
                 (pretty-properties/default-font-props       {:font-size :s :font-weight :normal})
                 (pretty-properties/default-outer-size-props {:outer-size-unit :full-block})
                 (pretty-properties/default-text-props       {:text-overflow :wrap :text-selectable? true})
                 (pretty-standards/standard-border-props)
                 (pretty-standards/standard-flex-props)
                 (pretty-standards/standard-font-props)
                 (pretty-standards/standard-inner-position-props)
                 (pretty-standards/standard-inner-size-props)
                 (pretty-standards/standard-outer-position-props)
                 (pretty-standards/standard-outer-size-props)
                 (pretty-standards/standard-text-props)
                ;(pretty-rules/apply-auto-border-crop)
                ;(pretty-rules/auto-disable-highlight-color)
                ;(pretty-rules/auto-disable-hover-color)
                 (pretty-rules/auto-count-content-lines)
                 (pretty-rules/auto-limit-multiline-count)
                 (pretty-rules/auto-set-multiline-height)
                 (pretty-rules/auto-align-scrollable-flex)
                 (pretty-rules/compose-content)))
