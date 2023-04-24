
(ns elements.notification-bubble.views
    (:require [elements.button.views                   :as button.views]
              [elements.icon-button.views              :as icon-button.views]
              [elements.notification-bubble.attributes :as notification-bubble.attributes]
              [elements.notification-bubble.prototypes :as notification-bubble.prototypes]
              [metamorphic-content.api                 :as metamorphic-content]
              [random.api                              :as random]))

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
  ; {:content (metamorphic-content)}
  [bubble-id {:keys [content] :as bubble-props}]
  [:div (notification-bubble.attributes/bubble-attributes bubble-id bubble-props)
        [:div (notification-bubble.attributes/bubble-body-attributes bubble-id bubble-props)
              [:div (notification-bubble.attributes/bubble-content-attributes bubble-id bubble-props)
                    [metamorphic-content/compose content]]
              [notification-bubble-secondary-button bubble-id bubble-props]
              [notification-bubble-primary-button   bubble-id bubble-props]]])

(defn element
  ; XXX#0731
  ; Some other items based on the notification-bubble element and their documentations link here.
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ; {:border-color (keyword)(opt)
  ;   :default, :highlight, :invert, :primary, :secondary, :success, :transparent, :warning
  ;  :border-position (keyword)(opt)
  ;   :all, :bottom, :top, :left, :right, :horizontal, :vertical
  ;  :border-radius (map)(opt)
  ;   {:tl (keyword)(opt)
  ;    :tr (keyword)(opt)
  ;    :br (keyword)(opt)
  ;    :bl (keyword)(opt)
  ;    :all (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :border-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :color (keyword or string)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :default
  ;  :class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)
  ;  :disabled? (boolean)(opt)
  ;  :font-size (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  ;   Default: :s
  ;  :font-weight (keyword)(opt)
  ;   :inherit, :thin, :extra-light, :light, :normal, :medium, :semi-bold, :bold, :extra-bold, :black
  ;   Default :medium
  ;  :fill-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :height (keyword)(opt)
  ;   :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :auto
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :max-height (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :max-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :min-height (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :min-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :outdent (map)(opt)
  ;   Same as the :indent property
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
  ;  :width (keyword)(opt)
  ;   :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :content}
  ;
  ; @usage
  ; [notification-bubble {...}]
  ;
  ; @usage
  ; [notification-bubble :my-notification-bubble {...}]
  ([bubble-props]
   [element (random/generate-keyword) bubble-props])

  ([bubble-id bubble-props]
   (let [bubble-props (notification-bubble.prototypes/bubble-props-prototype bubble-props)]
        [notification-bubble bubble-id bubble-props])))
