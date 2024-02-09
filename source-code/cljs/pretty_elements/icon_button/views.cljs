
(ns pretty-elements.icon-button.views
    (:require [fruits.random.api                      :as random]
              [pretty-elements.engine.api             :as pretty-elements.engine]
              [pretty-elements.icon-button.attributes :as icon-button.attributes]
              [pretty-elements.icon-button.prototypes :as icon-button.prototypes]
              [pretty-presets.engine.api              :as pretty-presets.engine]
              [reagent.api                            :as reagent]
              [pretty-accessories.api :as pretty-accessories]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- icon-button
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:badge (map)(opt)
  ;  :cover (map)(opt)
  ;  :icon (keyword)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :marker (map)(opt)}
  [button-id {:keys [badge cover icon label marker] :as button-props}]
  [:div (icon-button.attributes/button-attributes button-id button-props)
        [(pretty-elements.engine/clickable-auto-tag     button-id button-props)
         (icon-button.attributes/button-body-attributes button-id button-props)
         (if icon   [:i   (icon-button.attributes/button-icon-attributes  button-id button-props) icon])
         (if label  [:div (icon-button.attributes/button-label-attributes button-id button-props) label])
         (if badge  [pretty-accessories/badge  button-id badge])
         (if marker [pretty-accessories/marker button-id marker])
         (if cover  [pretty-accessories/cover  button-id cover])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  ; @note (tutorials#parameterizing)
  ; @note (pretty-elements.adornment.views#8097)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    button-id button-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount button-id button-props))
                       :component-did-update   (fn [%]   (pretty-elements.engine/element-did-update   button-id button-props %))
                       :reagent-render         (fn [_ button-props] [icon-button button-id button-props])}))

(defn view
  ; @param (keyword)(opt) button-id
  ; @param (map) button-props
  ; {:badge (map)(opt)
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
  ;  :height (keyword, px or string)(opt)
  ;  :highlighted? (boolean)(opt)
  ;  :highlight-color (keyword or string)(opt)
  ;  :highlight-pattern (keyword)(opt)
  ;  :hover-color (keyword or string)(opt)
  ;  :hover-pattern (keyword)(opt)
  ;  :hover-effect (keyword)(opt)
  ;  :href-target (keyword)(opt)
  ;  :href-uri (string)(opt)
  ;  :icon (keyword)
  ;  :icon-color (keyword or string)(opt)
  ;  :icon-family (keyword)(opt)
  ;  :icon-size (keyword, px or string)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :keypress (map)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :label-placeholder (metamorphic-content)(opt)
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
  ;  :progress (percentage)(opt)
  ;  :progress-color (keyword or string)
  ;  :progress-direction (keyword)(opt)
  ;  :progress-duration (ms)(opt)
  ;  :style (map)(opt)
  ;  :tab-disabled? (boolean)(opt)
  ;  :theme (keyword)(opt)
  ;  :tooltip (map)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [icon-button {...}]
  ;
  ; @usage
  ; [icon-button :my-button {...}]
  ([button-props]
   [view (random/generate-keyword) button-props])

  ([button-id button-props]
   ; @note (tutorials#parameterizing)
   (fn [_ button-props]
       (let [button-props (pretty-presets.engine/apply-preset            button-id button-props)
             button-props (icon-button.prototypes/button-props-prototype button-id button-props)
             button-props (pretty-elements.engine/element-timeout-props  button-id button-props)]
            [view-lifecycles button-id button-props]))))
