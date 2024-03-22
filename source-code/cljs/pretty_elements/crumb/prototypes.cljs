
(ns pretty-elements.crumb.prototypes
    (:require [pretty-models.api     :as pretty-models]
              [pretty-properties.api :as pretty-properties]
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
  (-> label (pretty-properties/default-font-props {:font-size :xs :font-weight :semi-bold})
            (pretty-properties/default-text-props {:text-overflow :ellipsis})))

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
       (-> props (pretty-properties/default-flex-props       {:gap :xs :orientation :horizontal})
                 (pretty-properties/default-outer-size-props {:max-width :l :outer-size-unit :full-block})
                 (pretty-models/click-control-standard-props)
                 (pretty-models/click-control-rules)
                 (pretty-models/flex-container-standard-props)
                 (pretty-models/flex-container-rules)
                 (pretty-subitems/apply-subitem-prototype :label label-prototype-f))))
