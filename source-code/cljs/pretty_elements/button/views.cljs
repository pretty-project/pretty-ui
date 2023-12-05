
(ns pretty-elements.button.views
    (:require [pretty-elements.button.attributes :as button.attributes]
              [pretty-elements.button.prototypes :as button.prototypes]
              [pretty-presets.api                :as pretty-presets]
              [random.api                        :as random]
              [re-frame.api                      :as r]
              [reagent.api                       :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- button-structure
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:href (string)(opt)
  ;  :icon (keyword)(opt)
  ;  :icon-position (keyword)(opt)
  ;  :label (string)(opt)
  ;  :on-click (Re-Frame metamorphic-event)(opt)}
  [button-id {:keys [href icon icon-position label on-click] :as button-props}]
  [:div (button.attributes/button-attributes button-id button-props)
        [(cond href :a on-click :button :else :div)
         (button.attributes/button-body-attributes button-id button-props)
         (case icon-position :left  [:<> (if icon  [:i   (button.attributes/button-icon-attributes  button-id button-props) icon])
                                         (if label [:div (button.attributes/button-label-attributes button-id button-props) label])]
                             :right [:<> (if label [:div (button.attributes/button-label-attributes button-id button-props) label])
                                         (if icon  [:i   (button.attributes/button-icon-attributes  button-id button-props) icon])]
                                    [:<> (if label [:div (button.attributes/button-label-attributes button-id button-props) label])])]])

(defn button
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  ; XXX#0106 (README.md#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (r/dispatch [:pretty-elements.button/button-did-mount    button-id button-props]))
                       :component-will-unmount (fn [_ _] (r/dispatch [:pretty-elements.button/button-will-unmount button-id button-props]))
                       :component-did-update   (fn [%]   (r/dispatch [:pretty-elements.button/button-did-update   button-id %]))
                       :reagent-render         (fn [_ button-props] [button-structure button-id button-props])}))

(defn element
  ; @info
  ; XXX#0714
  ; Some other items based on the 'button' element and their documentations link here.
  ;
  ; @warning
  ; BUG#9912
  ; If the keypress key-code is 13 (ENTER) the on-click event will fire multiple times during the key is pressed!
  ; This phenomenon caused by:
  ; 1. The keydown event focuses the button via the 'button.side-effects/key-pressed' function.
  ; 2. One of the default actions of the 13 (ENTER) key is to fire the on-click
  ;    function on a focused button element. Therefore, the 'on-click' function
  ;    fires repeatedly during the 13 (ENTER) key is pressed.
  ; In case of using any other key than the 13 (ENTER) the 'on-click' function fires only by
  ; the 'button.side-effects/key-released' function.
  ;
  ; @param (keyword)(opt) button-id
  ; @param (map) button-props
  ; {:badge-color (keyword)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :primary
  ;  :badge-content (metamorphic-content)(opt)
  ;  :badge-position (keyword)(opt)
  ;   :tl, :tr, :br, :bl, :left, :right, :bottom, :top
  ;   Default: :tr
  ;  :border-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
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
  ;  :class (keyword or keywords in vector)(opt)
  ;  :color (keyword or string)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :inherit
  ;  :cursor (keyword)(opt)
  ;   :default, :disabled, :grab, :grabbing, :move, :pointer, :progress}
  ;   Default: :pointer
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :font-size (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  ;   Default: :s
  ;  :font-weight (keyword)(opt)
  ;   :inherit, :thin, :extra-light, :light, :normal, :medium, :semi-bold, :bold, :extra-bold, :black
  ;   Default: :medium
  ;  :gap (keyword)(opt)
  ;   Distance between the icon and the label
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :auto
  ;  :horizontal-align (keyword)(opt)
  ;   :center, :left, :right
  ;   Default: :center
  ;  :hover-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :hover-effect (keyword)(opt)
  ;   :opacity
  ;  :href (string)(opt)
  ;  :icon (keyword)(opt)
  ;  :icon-color (keyword or string)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :inherit
  ;  :icon-family (keyword)(opt)
  ;   :material-symbols-filled, :material-symbols-outlined
  ;   Default: :material-symbols-outlined
  ;  :icon-position (keyword)(opt)
  ;   :left, :right
  ;   Default: :left
  ;  :icon-size (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  ;   Default: :s
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :keypress (map)(opt)
  ;   {:exclusive? (boolean)(opt)
  ;    :key-code (integer)
  ;    :required? (boolean)(opt)
  ;     Default: false}
  ;  :label (metamorphic-content)(opt)
  ;  :line-height (keyword)(opt)
  ;   :auto, :inherit, :text-block, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :text-block
  ;  :marker-color (keyword)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :marker-position (keyword)(opt)
  ;   :tl, :tr, :br, :bl, left, :right, bottom, :top
  ;  :on-click (Re-Frame metamorphic-event)(opt)
  ;  :on-mouse-over (Re-Frame metamorphic-event)(opt)
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
  ;  :preset (keyword)(opt)
  ;  :progress (percent)(opt)
  ;  :progress-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :muted
  ;  :progress-direction (keyword)(opt)
  ;   :ltr, :rtl, :ttb, :btt
  ;   Default: :ltr
  ;  :progress-duration (ms)(opt)
  ;   Default: 250
  ;  :text-overflow (keyword)(opt)
  ;   :ellipsis, :hidden, :wrap
  ;   Default: :no-wrap
  ;  :text-transform (keyword)(opt)
  ;   :capitalize, :lowercase, :uppercase
  ;  :tooltip-content (metamorphic-content)(opt)
  ;  :tooltip-position (keyword)(opt)
  ;   :left, :right
  ;  :width (keyword)(opt)
  ;   :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :content}
  ;
  ; @usage
  ; [button {...}]
  ;
  ; @usage
  ; [button :my-button {...}]
  ;
  ; @usage
  ; [button {:keypress {:key-code 13} :on-click [:my-event]}]
  ([button-props]
   [element (random/generate-keyword) button-props])

  ([button-id button-props]
   (fn [_ button-props] ; XXX#0106 (README.md#parametering)
       (let [button-props (pretty-presets/apply-preset              button-props)
             button-props (button.prototypes/button-props-prototype button-props)]
            [button button-id button-props]))))
