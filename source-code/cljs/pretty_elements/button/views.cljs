
(ns pretty-elements.button.views
    (:require [fruits.random.api                 :as random]
              [pretty-elements.button.attributes :as button.attributes]
              [pretty-elements.button.prototypes :as button.prototypes]
              [pretty-elements.engine.api        :as pretty-elements.engine]
              [pretty-presets.engine.api         :as pretty-presets.engine]
              [reagent.api                       :as reagent]
              [pretty-accessories.api :as pretty-accessories]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- button
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:badge (map)(opt)
  ;  :cover (map)(opt)
  ;  :icon (keyword)(opt)
  ;  :icon-position (keyword)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :marker (map)(opt)}
  [button-id {:keys [badge cover icon icon-position label marker] :as button-props}]
  [:div (button.attributes/button-attributes button-id button-props)
        [(pretty-elements.engine/clickable-auto-tag button-id button-props)
         (button.attributes/button-body-attributes  button-id button-props)
         (case icon-position :right [:<> (if label [:div (button.attributes/button-label-attributes button-id button-props) label])
                                         (if icon  [:i   (button.attributes/button-icon-attributes  button-id button-props) icon])]
                                    [:<> (if icon  [:i   (button.attributes/button-icon-attributes  button-id button-props) icon])
                                         (if label [:div (button.attributes/button-label-attributes button-id button-props) label])])
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
                       :reagent-render         (fn [_ button-props] [button button-id button-props])}))

(defn view
  ; @description
  ; Button element with optional keypress control, timeout lock, and progress display.
  ;
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
  ;  :font-size (keyword, px or string)(opt)
  ;  :font-weight (keyword or integer)(opt)
  ;  :gap (keyword, px or string)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :horizontal-align (keyword)(opt)
  ;  :highlighted? (boolean)(opt)
  ;  :highlight-color (keyword or string)(opt)
  ;  :highlight-pattern (keyword)(opt)
  ;  :hover-color (keyword or string)(opt)
  ;  :hover-pattern (keyword)(opt)
  ;  :hover-effect (keyword)(opt)
  ;  :href-target (keyword)(opt)
  ;  :href-uri (string)(opt)
  ;  :icon (keyword)(opt)
  ;  :icon-color (keyword or string)(opt)
  ;  :icon-family (keyword)(opt)
  ;  :icon-position (keyword)(opt)
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
  ;  :progress (percentage)(opt)
  ;  :progress-color (keyword or string)(opt)
  ;  :progress-direction (keyword)(opt)
  ;  :progress-duration (ms)(opt)
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
  ; [button {...}]
  ;
  ; @usage
  ; [button :my-button {...}]
  ([button-props]
   [view (random/generate-keyword) button-props])

  ([button-id button-props]
   ; @note (tutorials#parameterizing)
   (fn [_ button-props]
       (let [button-props (pretty-presets.engine/apply-preset           button-id button-props)
             button-props (button.prototypes/button-props-prototype     button-id button-props)
             button-props (pretty-elements.engine/element-timeout-props button-id button-props)]
            [view-lifecycles button-id button-props]))))
