
(ns pretty-elements.horizontal-separator.prototypes
    (:require [pretty-properties.api :as pretty-properties]
              [pretty-models.api :as pretty-models]
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
  (-> label (pretty-properties/default-font-props {:font-size :micro :font-weight :medium})
            (pretty-properties/default-text-props {:text-color :muted :text-transform :uppercase})))

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
                 (pretty-properties/default-line-props       {:line-color :muted :line-orientation :horizontal :line-size :grow})
                 (pretty-properties/default-outer-size-props {:outer-height :content :outer-width :auto :outer-size-unit :full-block})
                 (pretty-models/container-model-standard-props)
                 (pretty-models/container-model-rules)
                 (pretty-models/line-model-standard-props)
                 (pretty-models/line-model-rules)
                 (pretty-subitems/apply-subitem-prototype :label label-prototype-f))))
