
(ns pretty-elements.icon-button.views
    (:require [fruits.random.api                      :as random]
              [metamorphic-content.api                :as metamorphic-content]
              [pretty-elements.icon-button.attributes :as icon-button.attributes]
              [pretty-elements.icon-button.prototypes :as icon-button.prototypes]
              [pretty-presets.api                     :as pretty-presets]
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
  [button-id {:keys [href icon on-click label] :as button-props}]
  [:div (icon-button.attributes/button-attributes button-id button-props)
        [(cond href :a on-click :button :else :div)
         (icon-button.attributes/button-body-attributes button-id button-props)
         [:i (icon-button.attributes/button-icon-attributes button-id button-props) icon]]
        (if label [:div {:class :pe-icon-button--label :data-selectable false}
                        (metamorphic-content/compose label)])])

(defn- icon-button-lifecycles
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (r/dispatch [:pretty-elements.button/button-did-mount    button-id button-props]))
                       :component-will-unmount (fn [_ _] (r/dispatch [:pretty-elements.button/button-will-unmount button-id button-props]))
                       :component-did-update   (fn [%]   (r/dispatch [:pretty-elements.button/button-did-update   button-id %]))
                       :reagent-render         (fn [_ button-props] [icon-button button-id button-props])}))

(defn element
  ; @bug (pretty-elements.button.views#9912)
  ;
  ; @param (keyword)(opt) button-id
  ; @param (map) button-props
  ; {:badge-color (keyword or string)(opt)
  ;   Default: :primary
  ;  :badge-content (metamorphic-content)(opt)
  ;  :badge-position (keyword)(opt)
  ;   Default: :tr
  ;  :border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;   :all, :bottom, :top, :left, :right, :horizontal, :vertical
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :cursor (keyword or string)(opt)
  ;   Default: :pointer
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;   Default: :cover
  ;  :hover-color (keyword or string)(opt)
  ;  :hover-effect (keyword)(opt)
  ;  :href (string)(opt)
  ;  :icon (keyword)
  ;  :icon-color (keyword or string)(opt)
  ;   Default: :inherit
  ;  :icon-family (keyword)(opt)
  ;   Default: :material-symbols-outlined
  ;  :icon-size (keyword, px or string)(opt)
  ;   Default: :m
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :keypress (map)(constant)(opt)
  ;   {:exclusive? (boolean)(opt)
  ;     Exclusive keypress events temporarly disable every other previously registered keypress events.
  ;     Default: false
  ;    :key-code (integer)
  ;    :required? (boolean)(opt)
  ;     Only required keypress events remain active during a text-field is in focused state.
  ;     Default: false}
  ;  :label (metamorphic-content)(opt)
  ;  :marker-color (keyword or string)(opt)
  ;  :marker-position (keyword)(opt)
  ;  :on-click (function or Re-Frame metamorphic-event)(opt)
  ;  :on-mouse-over (function or Re-Frame metamorphic-event)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :progress (percent)(opt)
  ;  :progress-color (keyword or string)
  ;   Default: :muted
  ;  :progress-direction (keyword)(opt)
  ;   Default: :ltr
  ;  :progress-duration (ms)(opt)
  ;   Default: 250
  ;  :style (map)(opt)
  ;  :target (keyword)(opt)
  ;   :blank, :self
  ;  :tooltip-content (metamorphic-content)(opt)
  ;  :tooltip-position (keyword)(opt)}
  ;
  ; @usage
  ; [icon-button {...}]
  ;
  ; @usage
  ; [icon-button :my-button {...}]
  ;
  ; @usage
  ; [icon-button {:keypress {:key-code 13} :on-click [:my-event]}]
  ([button-props]
   [element (random/generate-keyword) button-props])

  ([button-id button-props]
   ; @note (tutorials#parametering)
   (fn [_ button-props]
       (let [button-props (pretty-presets/apply-preset                             button-props)
             button-props (icon-button.prototypes/button-props-prototype button-id button-props)]
            [icon-button-lifecycles button-id button-props]))))
