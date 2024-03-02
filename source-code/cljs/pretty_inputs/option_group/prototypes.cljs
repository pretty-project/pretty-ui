
(ns pretty-inputs.option-group.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]
              [pretty-subitems.api :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn group-props-prototype
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  [_ group-props]
  (-> group-props (pretty-properties/default-flex-props       {:gap :xs :orientation :vertical})
                  (pretty-properties/default-outer-size-props {:outer-size-unit :full-block})
                  (pretty-standards/standard-flex-props)
                  (pretty-standards/standard-inner-position-props)
                  (pretty-standards/standard-inner-size-props)
                  (pretty-standards/standard-outer-position-props)
                  
                  (pretty-standards/standard-input-option-props)

                  (pretty-standards/standard-outer-size-props)
                  (pretty-rules/auto-align-scrollable-flex)
                  (pretty-subitems/subitem-group<-subitem-default :options)
                  (pretty-subitems/subitem-group<-disabled-state  :options)
                  (pretty-subitems/leave-disabled-state           :options)))
