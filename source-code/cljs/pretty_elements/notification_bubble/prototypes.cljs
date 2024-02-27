
(ns pretty-elements.notification-bubble.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]
              [pretty-subitems.api :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bubble-props-prototype
  ; @ignore
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ;
  ; @return (map)
  [bubble-id bubble-props]
  (-> bubble-props (pretty-properties/default-flex-props       {:orientation :horizontal :horizontal-align :left :gap :auto})
                   (pretty-properties/default-font-props       {:font-size :s :font-weight :medium})
                   (pretty-properties/default-outer-size-props {:outer-size-unit :double-block})
                   (pretty-properties/default-text-props       {:text-overflow :wrap :text-selectable? true})
                   (pretty-standards/standard-border-props)
                   (pretty-standards/standard-flex-props)
                   (pretty-standards/standard-font-props)
                   (pretty-standards/standard-icon-props)
                   (pretty-standards/standard-inner-position-props)
                   (pretty-standards/standard-inner-size-props)
                   (pretty-standards/standard-outer-position-props)
                   (pretty-standards/standard-outer-size-props)
                   (pretty-standards/standard-text-props)
                   (pretty-rules/apply-auto-border-crop)
                   (pretty-rules/auto-align-scrollable-flex)
                  ;(pretty-rules/auto-disable-highlight-color)
                  ;(pretty-rules/auto-disable-hover-color)
                   (pretty-rules/compose-content)
                   (pretty-subitems/subitem-groups<-subitem-default :start-adornments :end-adornments)
                   (pretty-subitems/subitem-groups<-disabled-state  :start-adornments :end-adornments)
                   (pretty-subitems/leave-disabled-state            :start-adornments :end-adornments)))
