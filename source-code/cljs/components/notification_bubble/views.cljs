
(ns components.notification-bubble.views
    (:require [elements.api                              :as elements]
              [components.notification-bubble.prototypes :as notification-bubble.prototypes]
              [random.api                                :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; XXX#0731 (source-code/cljs/elements/notification_bubble/views.cljs)
  ; The 'notification-bubble' component is based on the 'notification-bubble' element.
  ; For more information check out the documentation of the 'notification-bubble' element.
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ; {:border-color (keyword)(opt)
  ;   Default: :secondary
  ;  :border-radius (map)(opt)
  ;   Default: {:all :m}
  ;  :border-width (keyword)(opt)
  ;   Default: :xs
  ;  :fill-color (keyword or string)(opt)
  ;   Default: :default
  ;  :indent (map)(opt)
  ;   Default: {:horizontal :xs :vertical :xs}
  ;  :min-width (keyword)(opt)
  ;   Default: :m
  ;  :outdent (map)(opt)
  ;   Default: {:bottom :xs :vertical :xs}
  ;  :primary-button (map)(opt)
  ;   Default: {:border-radius {:all :s}
  ;             :icon          :close
  ;             :hover-color   :highlight
  ;             :layout        :icon-button
  ;             :on-click      [:x.ui/remove-bubble! :my-bubble]}}
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
