
(ns components.notification-bubble.views
    (:require [components.notification-bubble.prototypes :as notification-bubble.prototypes]
              [fruits.random.api                         :as random]
              [pretty-elements.api                       :as pretty-elements]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; @info
  ; XXX#0731 (source-code/cljs/pretty_elements/notification_bubble/views.cljs)
  ; The 'notification-bubble' component is based on the 'notification-bubble' element.
  ; For more information, check out the documentation of the 'notification-bubble' element.
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ; {:border-color (keyword or string)(opt)
  ;   Default: :secondary
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;   Default: {:all :m}
  ;  :border-width (keyword, px or string)(opt)
  ;   Default: :xs
  ;  :fill-color (keyword or string)(opt)
  ;   Default: :default
  ;  :indent (map)(opt)
  ;   {:bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;   Default: {:horizontal :xs :vertical :xs}
  ;  :min-width (keyword)(opt)
  ;   Default: :m
  ;  :outdent (map)(opt)
  ;   {:bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
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
   ; @note (tutorials#parametering)
   (fn [_ bubble-props]
       (let [bubble-props (notification-bubble.prototypes/bubble-props-prototype bubble-id bubble-props)]
            [pretty-elements/notification-bubble bubble-id bubble-props]))))
