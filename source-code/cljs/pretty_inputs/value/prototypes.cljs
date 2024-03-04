
(ns pretty-inputs.value.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]
              [pretty-subitems.api :as pretty-subitems]
              [pretty-inputs.engine.api :as pretty-inputs.engine]))

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
  [id props label]
  (if-let [external-value (pretty-inputs.engine/get-input-external-value id props)]
          (-> label (assoc :content (str external-value)))
          (-> label)))

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
  (let [label-prototype-f (fn [%] (label-prototype id props %))]
       (-> props (pretty-properties/default-flex-props       {:orientation :horizontal})
                 (pretty-properties/default-outer-size-props {:outer-size-unit :full-block})
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
                 (pretty-rules/auto-set-mounted)
                 (pretty-subitems/ensure-subitem           :label)
                 (pretty-subitems/subitems<-disabled-state :start-adornment-group :end-adornment-group)
                 (pretty-subitems/leave-disabled-state     :start-adornment-group :end-adornment-group)
                 (pretty-subitems/apply-subitem-prototype  :label label-prototype-f))))
