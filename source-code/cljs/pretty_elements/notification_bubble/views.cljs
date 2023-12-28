
(ns pretty-elements.notification-bubble.views
    (:require [fruits.random.api                              :as random]
              [metamorphic-content.api                        :as metamorphic-content]
              [pretty-elements.button.views                   :as button.views]
              [pretty-elements.icon-button.views              :as icon-button.views]
              [pretty-elements.notification-bubble.attributes :as notification-bubble.attributes]
              [pretty-elements.notification-bubble.prototypes :as notification-bubble.prototypes]
              [pretty-presets.api                             :as pretty-presets]))

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
  ; {:content (metamorphic-content)(opt)}
  [bubble-id {:keys [content] :as bubble-props}]
  [:div (notification-bubble.attributes/bubble-attributes bubble-id bubble-props)
        [:div (notification-bubble.attributes/bubble-body-attributes bubble-id bubble-props)
              [:div (notification-bubble.attributes/bubble-content-attributes bubble-id bubble-props)
                    [metamorphic-content/compose content]]
              [notification-bubble-secondary-button bubble-id bubble-props]
              [notification-bubble-primary-button   bubble-id bubble-props]]])

(defn element
  ; @info
  ; XXX#0731
  ; Some other items based on the 'notification-bubble' element and their documentations link here.
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ; {:border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)(opt)
  ;  :content-value-f (function)(opt)
  ;   Default: return
  ;  :disabled? (boolean)(opt)
  ;  :font-size (keyword, px or string)(opt)
  ;   Default: :s
  ;  :font-weight (keyword or integer)(opt)
  ;   :inherit, :thin, :extra-light, :light, :normal, :medium, :semi-bold, :bold, :extra-bold, :black
  ;   Default :medium
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;   Default: :cover
  ;  :height (keyword, px or string)(opt)
  ;  :indent (map)(opt)
  ;   {:bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :max-height (keyword, px or string)(opt)
  ;  :max-width (keyword, px or string)(opt)
  ;  :min-height (keyword, px or string)(opt)
  ;  :min-width (keyword, px or string)(opt)
  ;  :outdent (map)(opt)
  ;   {:bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :placeholder (metamorphic-content)(opt)
  ;  :placeholder-value-f (function)(opt)
  ;   Default: return
  ;  :preset (keyword)(opt)
  ;  :primary-button (map)(opt)
  ;   {:layout (keyword)
  ;     :button, :icon-button
  ;     Default: :icon-button}
  ;  :secondary-button (map)(opt)
  ;   {:layout (keyword)
  ;     :button, :icon-button
  ;     Default: :icon-button}
  ;  :selectable? (boolean)(opt)
  ;   Default: false
  ;  :style (map)(opt)
  ;  :text-color (keyword or string)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :default
  ;  :width (keyword, px or string)(opt)}
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
       (let [bubble-props (pretty-presets/apply-preset                           bubble-props)
             bubble-props (notification-bubble.prototypes/bubble-props-prototype bubble-props)]
            [notification-bubble bubble-id bubble-props]))))
