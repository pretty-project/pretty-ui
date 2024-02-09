
(ns pretty-elements.image.views
    (:require [fruits.random.api                    :as random]
              [pretty-elements.engine.api           :as pretty-elements.engine]
              [pretty-elements.image.attributes :as image.attributes]
              [pretty-elements.image.prototypes :as image.prototypes]
              [pretty-presets.engine.api            :as pretty-presets.engine]
              [pretty-accessories.api :as pretty-accessories]
              [dynamic-props.api :as dynamic-props]
              [reagent.api :as reagent]
              [pretty-accessories.api :as pretty-accessories]
              [lazy-loader.api :as lazy-loader]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn broken-image
  ; @ignore
  []
  [:svg {:xmlns "http://www.w3.org/2000/svg" :height "24px" :width "24p0" :view-box "0 0 24 24" :fill "#000"}
        [:path {:fill "none" :d "M0 0h24v24H0z"}]
        [:path {:fill "none" :d "M0 0h24v24H0zm0 0h24v24H0zm21 19c0 1.1-.9 2-2 2H5c-1.1 0-2-.9-2-2V5c0-1.1.9-2 2-2h14c1.1 0 2 .9 2 2"}]
        [:path {             :d "M21 5v6.59l-3-3.01-4 4.01-4-4-4 4-3-3.01V5c0-1.1.9-2 2-2h14c1.1 0 2 .9 2 2zm-3 6.42l3 3.01V19c0 1.1-.9 2-2 2H5c-1.1 0-2-.9-2-2v-6.58l3 2.99 4-4 4 4 4-3.99z"}]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- image-load-sensor
  ; @ignore
  ;
  ; @param (keyword) image-id
  ; @param (map) image-props
  [image-id image-props]
  (let [sensor-props (image.prototypes/sensor-props-prototype image-id image-props)]
       [lazy-loader/image-sensor image-id sensor-props]))

(defn- image
  ; @ignore
  ;
  ; @param (keyword) image-id
  ; @param (map) image-props
  ; {:badge (map)(opt)
  ;  :cover (map)(opt)
  ;  :icon (keyword)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :loaded? (boolean)(opt)
  ;  :marker (map)(opt)}
  [image-id {:keys [badge cover icon label loaded? marker] :as image-props}]
  [:div (image.attributes/image-attributes image-id image-props)
        [image-load-sensor                 image-id image-props]
        [(pretty-elements.engine/clickable-auto-tag image-id image-props)
         (image.attributes/image-body-attributes    image-id image-props)
         (if loaded? [:div (image.attributes/image-canvas-attributes image-id image-props)]
                     [:i   (image.attributes/image-icon-attributes   image-id image-props) icon])
         (if label   [:div (image.attributes/image-label-attributes  image-id image-props) label])
         (if badge   [pretty-accessories/badge  image-id badge])
         (if marker  [pretty-accessories/marker image-id marker])
         (if cover   [pretty-accessories/cover  image-id cover])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) image-id
  ; @param (map) image-props
  [image-id image-props]
  ; @note (tutorials#parameterizing)
  ; @note (pretty-elements.adornment.views#8097)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    image-id image-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount image-id image-props))
                       :component-did-update   (fn [%]   (pretty-elements.engine/element-did-update   image-id image-props %))
                       :reagent-render         (fn [_ image-props] [image image-id image-props])}))

(defn view
  ; @description
  ; Optionally clickable image element with built-in lazy loader and animated loading icon.
  ;
  ; @param (keyword)(opt) image-id
  ; @param (map) image-props
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
  ;  :text-align (keyword)(opt)
  ;  :text-color (keyword or string)(opt)
  ;  :text-direction (keyword)(opt)
  ;  :text-overflow (keyword)(opt)
  ;  :text-selectable? (boolean)(opt)
  ;  :text-transform (keyword)(opt)
  ;  :theme (keyword)(opt)
  ;  :tooltip (map)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [image {...}]
  ;
  ; @usage
  ; [image :my-image {...}]
  ([image-props]
   [view (random/generate-keyword) image-props])

  ([image-id image-props]
   ; @note (tutorials#parameterizing)
   (fn [_ image-props]
       (let [image-props (pretty-presets.engine/apply-preset           image-id image-props)
             image-props (image.prototypes/image-props-prototype       image-id image-props)
             image-props (pretty-elements.engine/element-timeout-props image-id image-props)
             image-props (dynamic-props/import-props                   image-id image-props)]
            [view-lifecycles image-id image-props]))))
