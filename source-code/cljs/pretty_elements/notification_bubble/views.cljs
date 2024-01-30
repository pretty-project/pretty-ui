
(ns pretty-elements.notification-bubble.views
    (:require [fruits.random.api                              :as random]
              [metamorphic-content.api                        :as metamorphic-content]
              [pretty-elements.button.views                   :as button.views]
              [pretty-elements.icon-button.views              :as icon-button.views]
              [pretty-elements.notification-bubble.attributes :as notification-bubble.attributes]
              [pretty-elements.notification-bubble.prototypes :as notification-bubble.prototypes]
              [pretty-elements.engine.api                              :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.api                                    :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- notification-bubble-secondary-button
  ; @ignore
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ; {:secondary-button (map)(opt)}
  [bubble-id {{:keys [layout]} :secondary-button :keys [secondary-button] :as bubble-props}]
  (if secondary-button (case layout :button [button.views/element      secondary-button]
                                            [icon-button.views/element secondary-button])))

(defn- notification-bubble-primary-button
  ; @ignore
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ; {:primary-button (map)(opt)}
  [bubble-id {{:keys [layout]} :primary-button :keys [primary-button] :as bubble-props}]
  (if primary-button (case layout :button [button.views/element      primary-button]
                                          [icon-button.views/element primary-button])))

(defn- notification-bubble
  ; @ignore
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ; {:content (metamorphic-content)(opt)
  ;  :placeholder (metamorphic-content)(opt)}
  [bubble-id {:keys [content placeholder] :as bubble-props}]
  [:div (notification-bubble.attributes/bubble-attributes bubble-id bubble-props)
        [(pretty-elements.engine/clickable-auto-tag             bubble-id bubble-props)
         (notification-bubble.attributes/bubble-body-attributes bubble-id bubble-props)
         [:div (notification-bubble.attributes/bubble-content-attributes bubble-id bubble-props)
               [metamorphic-content/compose content placeholder]]
         [notification-bubble-secondary-button bubble-id bubble-props]
         [notification-bubble-primary-button   bubble-id bubble-props]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- element-lifecycles
  ; @ignore
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  [bubble-id bubble-props]
  ; @note (tutorials#parametering)
  ; @note (pretty-elements.adornment.views#8097)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    bubble-id bubble-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount bubble-id bubble-props))
                       :component-did-update   (fn [%]   (pretty-elements.engine/element-did-update   bubble-id bubble-props %))
                       :reagent-render         (fn [_ bubble-props] [notification-bubble bubble-id bubble-props])}))

(defn element
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ; {:border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :cursor (keyword or string)(opt)
  ;  :content (metamorphic-content)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :font-size (keyword, px or string)(opt)
  ;   Default: :s
  ;  :font-weight (keyword or integer)(opt)
  ;   Default :medium
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;   Default: :cover
  ;  :height (keyword, px or string)(opt)
  ;  :href-target (string)(opt)
  ;  :href-uri (string)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :keypress (map)(opt)
  ;  :max-height (keyword, px or string)(opt)
  ;  :max-width (keyword, px or string)(opt)
  ;  :min-height (keyword, px or string)(opt)
  ;  :min-width (keyword, px or string)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :placeholder (metamorphic-content)(opt)
  ;  :preset (keyword)(opt)
  ;  :primary-button (map)(opt)
  ;   {:layout (keyword)
  ;     :button, :icon-button
  ;     Default: :icon-button}
  ;  :secondary-button (map)(opt)
  ;   {:layout (keyword)
  ;     :button, :icon-button
  ;     Default: :icon-button}
  ;  :style (map)(opt)
  ;  :tab-disabled? (boolean)(opt)
  ;  :text-color (keyword or string)(opt)
  ;   Default: :default
  ;  :text-selectable? (boolean)(opt)
  ;  :theme (keyword)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; + end-adornments, start-adornments
  ; + keypress?
  ;
  ; @usage
  ; [notification-bubble {...}]
  ;
  ; @usage
  ; [notification-bubble :my-notification-bubble {...}]
  ([bubble-props]
   [element (random/generate-keyword) bubble-props])

  ([bubble-id bubble-props]
   ; @note (tutorials#parametering)
   (fn [_ bubble-props]
       (let [bubble-props (pretty-presets.engine/apply-preset                    bubble-id bubble-props)
             bubble-props (notification-bubble.prototypes/bubble-props-prototype bubble-id bubble-props)
             bubble-props (pretty-elements.engine/element-timeout-props          bubble-id bubble-props)]
            [element-lifecycles bubble-id bubble-props]))))

            ; + hover-color, hover-effect, hover-pattern ...
            ; + href-uri, href-target ...
            ; + on-click-f, on-click-timeout ...
