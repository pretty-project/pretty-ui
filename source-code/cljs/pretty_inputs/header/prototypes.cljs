
(ns pretty-inputs.header.prototypes
    (:require [fruits.vector.api               :as vector]
              [pretty-inputs.engine.api        :as pretty-inputs.engine]
              [pretty-inputs.header.adornments :as header.adornments]
              [pretty-properties.api           :as pretty-properties]
              [pretty-rules.api                :as pretty-rules]
              [pretty-standards.api            :as pretty-standards]
              [pretty-subitems.api             :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-props-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; @param (map) label
  ;
  ; @return (map)
  ; {:end-adornment-group (map)
  ;  :gap (keyword)
  ;  :label (multitype-content)}
  [id props label]
  ; The 'header' input implements the 'label' accessory, but it is replaced with the 'header' element which can display adornments.
  (if-let [toggle-info-text-adornment (header.adornments/toggle-info-text-adornment id props)]
          {:end-adornment-group {:adornments [toggle-info-text-adornment]}
           :gap :xs :label label}
          {:gap :xs :label label}))

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
  (let [on-mouse-up-f           (fn [_] (pretty-inputs.engine/focus-input! id props))
        label-props-prototype-f (fn [%] (label-props-prototype             id props %))]
       (-> props (pretty-properties/default-flex-props        {:horizontal-align :left :orientation :vertical})
                 (pretty-properties/default-mouse-event-props {:on-mouse-up-f on-mouse-up-f})
                 (pretty-properties/default-outer-size-props  {:outer-size-unit :full-block})
                 (pretty-standards/standard-flex-props)
                 (pretty-standards/standard-inner-position-props)
                 (pretty-standards/standard-inner-size-props)
                 (pretty-standards/standard-outer-position-props)
                 (pretty-standards/standard-outer-size-props)
                 (pretty-rules/auto-align-scrollable-flex)
                 (pretty-rules/auto-disable-mouse-events)
                 (pretty-rules/auto-set-mounted)
                 (pretty-subitems/ensure-subitem          :label)
                 (pretty-subitems/apply-subitem-prototype :label label-props-prototype-f))))
