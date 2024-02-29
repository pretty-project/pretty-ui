
(ns pretty-elements.header.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]
              [pretty-subitems.api :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-props-prototype
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;
  ; @return (map)
  [header-id header-props]
  (-> header-props (pretty-properties/default-flex-props       {:orientation :horizontal})
                   (pretty-properties/default-outer-size-props {:outer-size-unit :full-block})
                   (pretty-standards/standard-border-props)
                   (pretty-standards/standard-flex-props)
                   (pretty-standards/standard-inner-position-props)
                   (pretty-standards/standard-inner-size-props)
                   (pretty-standards/standard-outer-position-props)
                   (pretty-standards/standard-outer-size-props)
                   (pretty-rules/apply-auto-border-crop)
                   (pretty-rules/auto-align-scrollable-flex)
                  ;(pretty-rules/auto-disable-highlight-color)
                  ;(pretty-rules/auto-disable-hover-color)
                   (pretty-subitems/subitems<-disabled-state :start-adornment-group :end-adornment-group)
                   (pretty-subitems/leave-disabled-state     :start-adornment-group :end-adornment-group)))
