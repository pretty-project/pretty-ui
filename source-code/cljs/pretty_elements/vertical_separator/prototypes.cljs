
(ns pretty-elements.vertical-separator.prototypes
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
       (-> props (pretty-properties/default-flex-props       {:gap :xs :orientation :vertical})
                 (pretty-properties/default-line-props       {:line-color :muted :line-orientation :vertical :line-size :grow})
                 (pretty-properties/default-outer-size-props {:outer-height :parent :outer-size-unit :full-block :outer-width :content})
                 (pretty-models/flex-container-standard-props)
                 (pretty-models/flex-container-rules)
                 (pretty-models/line-canvas-standard-props)
                 (pretty-models/line-canvas-rules)
                 (pretty-subitems/apply-subitem-prototype :label label-prototype-f))))
