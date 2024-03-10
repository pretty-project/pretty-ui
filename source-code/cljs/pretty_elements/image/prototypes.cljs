
(ns pretty-elements.image.prototypes
    (:require [dynamic-props.api     :as dynamic-props]
              [pretty-properties.api :as pretty-properties]
              [pretty-models.api :as pretty-models]
              [pretty-subitems.api   :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sensor-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:background-uri (string)(opt)
  ;  ...}
  ; @param (map) sensor
  ;
  ; @return (map)
  ; {:on-load-f (function)
  ;  :uri (string)}
  [id {:keys [background-uri]} _]
  (let [on-load-f (fn [_] (dynamic-props/merge-props! id {:animation-duration nil :animation-name nil :animation-repeat nil})
                          (dynamic-props/merge-props! id {:loaded? true}))]
       {:on-load-f on-load-f
        :uri       background-uri}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn icon-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; @param (map) icon
  ;
  ; @return (map)
  [_ _ icon]
  (-> icon (pretty-properties/default-icon-props       {:icon-name :image :icon-color :muted})
           (pretty-properties/default-outer-size-props {:outer-height :grow :outer-width :parent})))

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
  (-> label (pretty-properties/default-font-props {:font-size :xs :font-weight :medium})
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
  (let [icon-prototype-f   (fn [%] (icon-prototype   id props %))
        label-prototype-f  (fn [%] (label-prototype  id props %))
        sensor-prototype-f (fn [%] (sensor-prototype id props %))]
       (-> props (pretty-properties/default-animation-props        {:animation-duration 2000 :animation-mode :repeat :animation-name :pulse})
                 (pretty-properties/default-background-color-props {:fill-color :highlight})
                 (pretty-properties/default-background-image-props {:background-size :contain})
                 (pretty-properties/default-border-props           {:border-crop :auto})
                 (pretty-properties/default-content-size-props     {:content-height :grow :content-width :parent})
                 (pretty-properties/default-flex-props             {:orientation :vertical})
                 (pretty-properties/default-outer-size-props       {:outer-height :s :outer-width :s :outer-size-unit :double-block})
                 (pretty-models/click-control-standard-props)
                 (pretty-models/click-control-rules)
                 (pretty-models/flex-container-standard-props)
                 (pretty-models/flex-container-rules)
                 (pretty-models/image-canvas-standard-props)
                 (pretty-models/image-canvas-rules)
                 (pretty-subitems/ensure-subitems         :icon   :sensor)
                 (pretty-subitems/apply-subitem-prototype :icon   icon-prototype-f)
                 (pretty-subitems/apply-subitem-prototype :label  label-prototype-f)
                 (pretty-subitems/apply-subitem-prototype :sensor sensor-prototype-f))))
