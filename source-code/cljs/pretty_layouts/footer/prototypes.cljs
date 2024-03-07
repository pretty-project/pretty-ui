
(ns pretty-layouts.footer.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api      :as pretty-rules]
              [pretty-standards.api  :as pretty-standards]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn props-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  [_ props]
  (-> props (pretty-properties/default-flex-props       {:orientation :horizontal})
            (pretty-properties/default-font-props       {:font-size :s :font-weight :medium})
            (pretty-properties/default-outer-size-props {:outer-height :content :outer-width :parent :outer-size-unit :half-block})
            (pretty-properties/default-text-props       {:text-selectable? false})
            (pretty-standards/standard-border-props)
            (pretty-standards/standard-flex-props)
            (pretty-standards/standard-font-props)
            (pretty-standards/standard-inner-position-props)
            (pretty-standards/standard-inner-size-props)
            (pretty-standards/standard-outer-position-props)
            (pretty-standards/standard-outer-size-props)
            (pretty-standards/standard-text-props)
            (pretty-rules/apply-auto-border-crop)
            (pretty-rules/auto-align-scrollable-flex)
            (pretty-rules/auto-disable-mouse-events)
            (pretty-rules/auto-set-mounted)
            (pretty-rules/compose-content)))
