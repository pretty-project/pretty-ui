
(ns pretty-inputs.value.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]
              [pretty-subitems.api :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn value-props-prototype
  ; @ignore
  ;
  ; @param (keyword) value-id
  ; @param (map) value-props
  ;
  ; @return (map)
  [value-id value-props]
  (-> value-props (pretty-properties/default-flex-props       {:orientation :horizontal})
                  (pretty-properties/default-font-props       {:font-size :s :font-weight :medium})
                  (pretty-properties/default-outer-size-props {:outer-size-unit :full-block})
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
                 ;(pretty-rules/auto-disable-highlight-color)
                 ;(pretty-rules/auto-disable-hover-color)
                  (pretty-subitems/subitem-groups<-subitem-default :start-adornments :end-adornments)
                  (pretty-subitems/subitem-groups<-disabled-state  :start-adornments :end-adornments)
                  (pretty-subitems/leave-disabled-state            :start-adornments :end-adornments)))
