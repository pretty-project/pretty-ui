
(ns pretty-inputs.header.prototypes
    (:require [pretty-inputs.header.adornments :as header.adornments]
              [pretty-properties.api           :as pretty-properties]
              [pretty-models.api :as pretty-models]
              [pretty-subitems.api             :as pretty-subitems]))

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
  (let [label-prototype-f (fn [%] (label-prototype id props %))]
       (-> props (pretty-properties/default-flex-props        {:horizontal-align :left :orientation :vertical})
                 (pretty-properties/default-outer-size-props  {:outer-size-unit :full-block})
                 (pretty-models/flex-container-standard-props)
                 (pretty-models/flex-container-rules)
                 (pretty-subitems/ensure-subitems         :label)
                 (pretty-subitems/apply-subitem-prototype :label label-prototype-f))))
