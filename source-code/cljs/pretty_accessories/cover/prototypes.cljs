
(ns pretty-accessories.cover.prototypes
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
  (-> label (pretty-properties/default-font-props {:font-size :s :font-weight :semi-bold})))

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
       (-> props (pretty-properties/default-background-color-props {:fill-color :default})
                 (pretty-properties/default-flex-props             {:orientation :horizontal})
                 (pretty-properties/default-outer-position-props   {:outer-position :tl :outer-position-method :absolute})
                 (pretty-properties/default-outer-size-props       {:outer-height :parent :outer-width :parent :outer-size-unit :quarter-block})
                 (pretty-properties/default-visibility-props       {:opacity :hard})
                 (pretty-models/content-model-standard-props)
                 (pretty-models/content-model-rules)
                 (pretty-subitems/apply-subitem-prototype :label label-prototype-f))))
