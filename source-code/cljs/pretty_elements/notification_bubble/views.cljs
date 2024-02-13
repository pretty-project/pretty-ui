
(ns pretty-elements.notification-bubble.views
    (:require [fruits.random.api                              :as random]
              [pretty-elements.engine.api                     :as pretty-elements.engine]
              [pretty-elements.adornment-group.views :as adornment-group.views]
              [pretty-elements.notification-bubble.attributes :as notification-bubble.attributes]
              [pretty-elements.notification-bubble.prototypes :as notification-bubble.prototypes]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.core :as reagent]
              [pretty-accessories.api :as pretty-accessories]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- notification-bubble
  ; @ignore
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ; {:content (metamorphic-content)(opt)
  ;  :cover (map)(opt)
  ;  :end-adornments (map)(opt)
  ;  :start-adornments (map)(opt)}
  [bubble-id {:keys [content cover end-adornments start-adornments] :as bubble-props}]
  [:div (notification-bubble.attributes/bubble-attributes bubble-id bubble-props)
        [(pretty-elements.engine/clickable-auto-tag             bubble-id bubble-props)
         (notification-bubble.attributes/bubble-body-attributes bubble-id bubble-props)
         (when start-adornments [adornment-group.views/view bubble-id {:adornments start-adornments}])
         (when :always          [:div (notification-bubble.attributes/bubble-content-attributes bubble-id bubble-props) content])
         (when end-adornments   [adornment-group.views/view bubble-id {:adornments end-adornments}])
         (when cover            [pretty-accessories/cover   bubble-id cover])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  [bubble-id bubble-props]
  ; @note (tutorials#parameterizing)
  ; @note (pretty-elements.adornment.views#8097)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    bubble-id bubble-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount bubble-id bubble-props))
                         :component-did-update   (fn [%]   (pretty-elements.engine/element-did-update   bubble-id bubble-props %))
                         :reagent-render         (fn [_ bubble-props] [notification-bubble bubble-id bubble-props])}))

(defn view
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ; {:border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :cover (map)(opt)
  ;  :cursor (keyword or string)(opt)
  ;  :content (metamorphic-content)(opt)
  ;  :content-placeholder (metamorphic-content)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :end-adornment-default (map)(opt)
  ;  :end-adornments (maps in vector)(opt)
  ;  :font-size (keyword, px or string)(opt)
  ;  :font-weight (keyword or integer)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :href-target (string)(opt)
  ;  :href-uri (string)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :keypress (map)(opt)
  ;  :letter-spacing (keyword, px or string)(opt)
  ;  :line-height (keyword, px or string)(opt)
  ;  :max-height (keyword, px or string)(opt)
  ;  :max-width (keyword, px or string)(opt)
  ;  :min-height (keyword, px or string)(opt)
  ;  :min-width (keyword, px or string)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :progress (percentage)(opt)
  ;  :progress-color (keyword or string)(opt)
  ;  :progress-direction (keyword)(opt)
  ;  :progress-duration (ms)(opt)
  ;  :start-adornment-default (map)(opt)
  ;  :start-adornments (maps in vector)(opt)
  ;  :style (map)(opt)
  ;  :tab-disabled? (boolean)(opt)
  ;  :text-align (keyword)(opt)
  ;  :text-color (keyword or string)(opt)
  ;  :text-direction (keyword)(opt)
  ;  :text-overflow (keyword)(opt)
  ;  :text-selectable? (boolean)(opt)
  ;  :text-transform (keyword)(opt)
  ;  :theme (keyword)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; flex props
  ; + hover-color, hover-effect, hover-pattern, highlight-... ...
  ; + href-uri, href-target ...
  ; + on-click-f, on-click-timeout ...
  ;
  ; @usage
  ; [notification-bubble {...}]
  ;
  ; @usage
  ; [notification-bubble :my-notification-bubble {...}]
  ([bubble-props]
   [view (random/generate-keyword) bubble-props])

  ([bubble-id bubble-props]
   ; @note (tutorials#parameterizing)
   (fn [_ bubble-props]
       (let [bubble-props (pretty-presets.engine/apply-preset                            bubble-id bubble-props)
             bubble-props (notification-bubble.prototypes/bubble-props-prototype         bubble-id bubble-props)
             bubble-props (pretty-elements.engine/element-timeout-props                  bubble-id bubble-props)
             bubble-props (pretty-elements.engine/element-subitem-group<-subitem-default bubble-id bubble-props :start-adornments :start-adornment-default)
             bubble-props (pretty-elements.engine/element-subitem-group<-subitem-default bubble-id bubble-props :start-adornments :start-adornment-default)
             bubble-props (pretty-elements.engine/element-subitem-group<-disabled-state  bubble-id bubble-props :end-adornments)
             bubble-props (pretty-elements.engine/element-subitem-group<-disabled-state  bubble-id bubble-props :end-adornments)]
            [view-lifecycles bubble-id bubble-props]))))
