
(ns pretty-elements.thumbnail.prototypes
    (:require [pretty-elements.image.prototypes :as image.prototypes]
              [pretty-properties.api            :as pretty-properties]
              [pretty-models.api :as pretty-models]
              [pretty-subitems.api              :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (pretty-elements.image.prototypes/*)
(def sensor-prototype image.prototypes/sensor-prototype)
(def icon-prototype   image.prototypes/icon-prototype)
(def label-prototype  image.prototypes/label-prototype)

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
  (let [icon-prototype-f   (fn [%] (icon-prototype   id props %))
        label-prototype-f  (fn [%] (label-prototype  id props %))
        sensor-prototype-f (fn [%] (sensor-prototype id props %))]
       (-> props (pretty-properties/default-animation-props        {:animation-duration 2000 :animation-mode :repeat :animation-name :pulse})
                 (pretty-properties/default-background-color-props {:fill-color :highlight})
                 (pretty-properties/default-background-image-props {:background-size :contain})
                 (pretty-properties/default-border-props           {:border-crop :auto})
                 (pretty-properties/default-content-size-props     {:content-height :grow :content-width :parent})
                 (pretty-properties/default-flex-props             {:orientation :vertical})
                 (pretty-properties/default-outer-size-props       {:outer-height :s :outer-width :s :outer-size-unit :full-block})
                 (pretty-models/clickable-model-standard-props)
                 (pretty-models/clickable-model-rules)
                 (pretty-models/container-model-standard-props)
                 (pretty-models/container-model-rules)
                 (pretty-models/image-model-standard-props)
                 (pretty-models/image-model-rules)
                 (pretty-subitems/ensure-subitems         :icon   :sensor)
                 (pretty-subitems/apply-subitem-prototype :icon   icon-prototype-f)
                 (pretty-subitems/apply-subitem-prototype :label  label-prototype-f)
                 (pretty-subitems/apply-subitem-prototype :sensor sensor-prototype-f))))
