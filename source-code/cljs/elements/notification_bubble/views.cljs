
(ns elements.notification-bubble.views
    (:require [elements.button.views                   :as button.views]
              [elements.icon-button.views              :as icon-button.views]
              [elements.notification-bubble.helpers    :as notification-bubble.helpers]
              [elements.notification-bubble.prototypes :as notification-bubble.prototypes]
              [random.api                              :as random]
              [x.components.api                        :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- notification-bubble-secondary-button
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ; {:secondary-button (map)(opt)}
  [bubble-id {{:keys [layout]} :secondary-button :keys [secondary-button] :as bubble-props}]
  (if secondary-button (case layout :button [button.views/element      secondary-button]
                                            [icon-button.views/element secondary-button])))

(defn- notification-bubble-primary-button
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ; {:primary-button (map)(opt)}
  [bubble-id {{:keys [layout]} :primary-button :keys [primary-button] :as bubble-props}]
  (if primary-button (case layout :button [button.views/element      primary-button]
                                          [icon-button.views/element primary-button])))

(defn- notification-bubble-content
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ; {:content (metamorphic-content)}
  [bubble-id {:keys [content] :as bubble-props}]
  [:div.e-notification-bubble--content (notification-bubble.helpers/bubble-content-attributes bubble-id bubble-props)
                                       [x.components/content                                  bubble-id content]])

(defn- notification-bubble-body
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  [bubble-id bubble-props]
  [:div.e-notification-bubble--body (notification-bubble.helpers/bubble-body-attributes bubble-id bubble-props)
                                    [notification-bubble-content                        bubble-id bubble-props]
                                    [notification-bubble-secondary-button               bubble-id bubble-props]
                                    [notification-bubble-primary-button                 bubble-id bubble-props]])

(defn- notification-bubble
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  [bubble-id bubble-props]
  [:div.e-notification-bubble (notification-bubble.helpers/bubble-attributes bubble-id bubble-props)
                              [notification-bubble-body                      bubble-id bubble-props]])

(defn element
  ; XXX#0731
  ; Some other items based on the notification-bubble element and their documentations are linked to here.
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
  ;   :inherit, :extra-light, :light, :normal, :medium, :bold, :extra-bold
  ;   Default :medium
  ;  :fill-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
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
  ;  :style (map)(opt)}
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
