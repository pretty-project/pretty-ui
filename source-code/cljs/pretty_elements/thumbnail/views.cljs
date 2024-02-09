
(ns pretty-elements.thumbnail.views
    (:require [fruits.random.api                    :as random]
              [pretty-elements.engine.api           :as pretty-elements.engine]
              [pretty-elements.thumbnail.attributes :as thumbnail.attributes]
              [pretty-elements.thumbnail.prototypes :as thumbnail.prototypes]
              [pretty-presets.engine.api            :as pretty-presets.engine]
              [pretty-accessories.api :as pretty-accessories]
              [dynamic-props.api :as dynamic-props]
              [reagent.api :as reagent]
              [pretty-accessories.api :as pretty-accessories]
              [lazy-loader.api :as lazy-loader]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- thumbnail-sensor
  ; @ignore
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  [thumbnail-id thumbnail-props]
  (let [sensor-props (thumbnail.prototypes/sensor-props-prototype thumbnail-id thumbnail-props)]
       [lazy-loader/image-sensor thumbnail-id sensor-props]))

(defn- thumbnail
  ; @ignore
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ; {:badge (map)(opt)
  ;  :cover (map)(opt)
  ;  :icon (keyword)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :loaded? (boolean)(opt)
  ;  :marker (map)(opt)}
  [thumbnail-id {:keys [badge cover icon label loaded? marker] :as thumbnail-props}]
  [:div (thumbnail.attributes/thumbnail-attributes thumbnail-id thumbnail-props)
        [thumbnail-sensor                          thumbnail-id thumbnail-props]
        [(pretty-elements.engine/clickable-auto-tag      thumbnail-id thumbnail-props)
         (thumbnail.attributes/thumbnail-body-attributes thumbnail-id thumbnail-props)
         (if loaded? [:div (thumbnail.attributes/thumbnail-canvas-attributes thumbnail-id thumbnail-props)]
                     [:i   (thumbnail.attributes/thumbnail-icon-attributes   thumbnail-id thumbnail-props) icon])
         (if label   [:div (thumbnail.attributes/thumbnail-label-attributes  thumbnail-id thumbnail-props) label])
         (if badge   [pretty-accessories/badge  thumbnail-id badge])
         (if marker  [pretty-accessories/marker thumbnail-id marker])
         (if cover   [pretty-accessories/cover  thumbnail-id cover])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  [thumbnail-id thumbnail-props]
  ; @note (tutorials#parameterizing)
  ; @note (pretty-elements.adornment.views#8097)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    thumbnail-id thumbnail-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount thumbnail-id thumbnail-props))
                       :component-did-update   (fn [%]   (pretty-elements.engine/element-did-update   thumbnail-id thumbnail-props %))
                       :reagent-render         (fn [_ thumbnail-props] [thumbnail thumbnail-id thumbnail-props])}))

(defn view
  ; @param (keyword)(opt) thumbnail-id
  ; @param (map) thumbnail-props
  ; {:animation-duration (ms)(opt)
  ;  :animation-mode (keyword)(opt)
  ;  :animation-name (keyword or string)(opt)
  ;  :background-size (keyword)(opt)
  ;  :background-uri (string)(opt)
  ;  :badge (map)(opt)
  ;  :border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :click-effect (keyword)(opt)
  ;  :cover (map)(opt)
  ;  :cursor (keyword or string)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;  :font-size (keyword, px or string)(opt)
  ;  :font-weight (keyword or integer)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :highlighted? (boolean)(opt)
  ;  :highlight-color (keyword or string)(opt)
  ;  :highlight-pattern (keyword)(opt)
  ;  :href-target (keyword)(opt)
  ;  :href-uri (string)(opt)
  ;  :hover-color (keyword or string)(opt)
  ;  :hover-pattern (keyword)(opt)
  ;  :hover-effect (keyword)(opt)
  ;  :icon (keyword)(opt)
  ;  :icon-color (keyword or string)(opt)
  ;  :icon-family (keyword)(opt)
  ;  :icon-size (keyword, px or string)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :keypress (map)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :label-placeholder (metamorphic-content)(opt)
  ;  :letter-spacing (keyword, px or string)(opt)
  ;  :line-height (keyword, px or string)(opt)
  ;  :marker (map)(opt)
  ;  :max-height (keyword, px or string)(opt)
  ;  :max-width (keyword, px or string)(opt)
  ;  :min-height (keyword, px or string)(opt)
  ;  :min-width (keyword, px or string)(opt)
  ;  :on-click-f (function)(opt)
  ;  :on-click-timeout (ms)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :tab-disabled? (boolean)(opt)
  ;  :text-color (keyword or string)(opt)
  ;  :text-overflow (keyword)(opt)
  ;  :text-transform (keyword)(opt)
  ;  :theme (keyword)(opt)
  ;  :tooltip (map)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [thumbnail {...}]
  ;
  ; @usage
  ; [thumbnail :my-thumbnail {...}]
  ([thumbnail-props]
   [view (random/generate-keyword) thumbnail-props])

  ([thumbnail-id thumbnail-props]
   ; @note (tutorials#parameterizing)
   (fn [_ thumbnail-props]
       (let [thumbnail-props (pretty-presets.engine/apply-preset             thumbnail-id thumbnail-props)
             thumbnail-props (thumbnail.prototypes/thumbnail-props-prototype thumbnail-id thumbnail-props)
             thumbnail-props (pretty-elements.engine/element-timeout-props   thumbnail-id thumbnail-props)
             thumbnail-props (dynamic-props/import-props                     thumbnail-id thumbnail-props)]
            [view-lifecycles thumbnail-id thumbnail-props]))))
