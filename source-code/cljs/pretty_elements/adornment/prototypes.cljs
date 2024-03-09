
(ns pretty-elements.adornment.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-models.api  :as pretty-models]
              [pretty-subitems.api   :as pretty-subitems]))

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
  [_ _ label]
  (-> label (pretty-properties/default-font-props {:font-size :xxs :font-weight :medium})))

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
                 (pretty-properties/default-outer-size-props {:min-outer-width :xs :outer-size-unit :half-block})
                 (pretty-models/clickable-model-standard-props)
                 (pretty-models/clickable-model-rules)
                 (pretty-models/container-model-standard-props)
                 (pretty-models/container-model-rules)
                 (pretty-subitems/apply-subitem-prototype :label label-prototype-f))))
