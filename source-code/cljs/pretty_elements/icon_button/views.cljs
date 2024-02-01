
(ns pretty-elements.icon-button.views
    (:require [fruits.random.api                      :as random]
              [metamorphic-content.api                :as metamorphic-content]
              [pretty-elements.icon-button.attributes :as icon-button.attributes]
              [pretty-elements.icon-button.prototypes :as icon-button.prototypes]
              [pretty-elements.engine.api                      :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [re-frame.api                           :as r]
              [reagent.api                            :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- icon-button
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:icon (keyword)
  ;  :label (metamorphic-content)(opt)}
  [button-id {:keys [icon label] :as button-props}]
  [:div (icon-button.attributes/button-attributes button-id button-props)
        [(pretty-elements.engine/clickable-auto-tag     button-id button-props)
         (icon-button.attributes/button-body-attributes button-id button-props)
         [:i (icon-button.attributes/button-icon-attributes button-id button-props) icon]]
        (if label [:div {:class :pe-icon-button--label :data-text-selectable false}
                        (metamorphic-content/compose label)])])

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
  ; {:badge-color (keyword or string)(opt)
  ;  :badge-content (metamorphic-content)(opt)
  ;  :badge-position (keyword)(opt)
  ;  :border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :click-effect (keyword)(opt)
  ;  :cursor (keyword or string)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
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
  ;  :marker-color (keyword or string)(opt)
  ;  :marker-position (keyword)(opt)
  ;  :on-click-f (function)(opt)
  ;  :on-click-timeout (ms)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :progress (percent)(opt)
  ;  :progress-color (keyword or string)
  ;  :progress-direction (keyword)(opt)
  ;  :progress-duration (ms)(opt)
  ;  :style (map)(opt)
  ;  :tab-disabled? (boolean)(opt)
  ;  :theme (keyword)(opt)
  ;  :tooltip-content (metamorphic-content)(opt)
  ;  :tooltip-position (keyword)(opt)}
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
