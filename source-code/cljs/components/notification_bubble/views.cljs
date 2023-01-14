
(ns components.notification-bubble.views
    (:require [elements.api                              :as elements]
              [components.notification-bubble.prototypes :as notification-bubble.prototypes]
              [random.api                                :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ; {:border-color (keyword)(opt)
  ;   :default, :highlight, :invert, :primary, :secondary, :success, :transparent, :warning
  ;   Default: :secondary
  ;  :border-radius (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :m
  ;  :border-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :xs
  ;  :class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :default
  ;  :indent (map)(opt)
  ;   Default: {:horizontal :xs :vertical :xs}
  ;  :min-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :m
  ;  :outdent (map)(opt)
  ;   Default: {:bottom :xs :vertical :xs}
  ;  :primary-button (map)(opt)
  ;   {:layout (keyword)(opt)
  ;     :button, :icon-button
  ;     Default: :icon-button}
  ;   Default: {:border-radius :s
  ;             :icon          :close
  ;             :hover-color   :highlight
  ;             :layout        :icon-button
  ;             :on-click      [:x.ui/remove-bubble! :my-bubble]}
  ;  :secondary-button (map)(opt)
  ;   {:layout (keyword)(opt)
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
   [component (random/generate-keyword) bubble-props])

  ([bubble-id bubble-props]
   (let [bubble-props (notification-bubble.prototypes/bubble-props-prototype bubble-id bubble-props)]
        [elements/notification-bubble bubble-id bubble-props])))
