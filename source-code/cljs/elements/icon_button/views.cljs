
(ns elements.icon-button.views
    (:require [elements.icon-button.attributes :as icon-button.attributes]
              [elements.icon-button.prototypes :as icon-button.prototypes]
              [metamorphic-content.api         :as metamorphic-content]
              [random.api                      :as random]
              [re-frame.api                    :as r]
              [reagent.api                     :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- icon-button-structure
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
        (if label [:div {:class :e-icon-button--label :data-selectable false}
                        (metamorphic-content/compose label)])])

(defn- icon-button
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (r/dispatch [:elements.button/button-did-mount    button-id button-props]))
                       :component-will-unmount (fn [_ _] (r/dispatch [:elements.button/button-will-unmount button-id button-props]))
                       :component-did-update   (fn [%]   (r/dispatch [:elements.button/button-did-update   button-id %]))
                       :reagent-render         (fn [_ button-props] [icon-button-structure button-id button-props])}))

(defn element
  ; @warning
  ; BUG#9912 (source-code/cljs/elements/button.views)
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
  ;  :cursor (keyword)(opt)
  ;   :default, :disabled, :grab, :grabbing, :move, :pointer, :progress
  ;   Default: :pointer
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :hover-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :hover-effect (keyword)(opt)
  ;   :opacity
  ;  :href (string)(opt)
  ;  :icon (keyword)
  ;  :icon-color (keyword or string)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :inherit
  ;  :icon-family (keyword)(opt)
  ;   :material-symbols-filled, :material-symbols-outlined
  ;   Default: :material-symbols-filled
  ;  :icon-size (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :m
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :keypress (map)(constant)(opt)
  ;   {:exclusive? (boolean)(opt)
  ;    :key-code (integer)
  ;    :required? (boolean)(opt)
  ;     Default: false}
  ;  :label (metamorphic-content)(opt)
  ;  :marker-color (keyword)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :marker-position (keyword)(opt)
  ;   :tl, :tr, :br, :bl, left, :right, bottom, :top
  ;  :on-click (Re-Frame metamorphic-event)(opt)
  ;  :on-mouse-over (Re-Frame metamorphic-event)(opt)
  ;  :outdent (map)(opt)
  ;   Same as the :indent property
  ;  :progress (percent)(opt)
  ;  :progress-color (keyword or string)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :muted
  ;  :progress-direction (keyword)(opt)
  ;   :ltr, :rtl, :ttb, :btt
  ;   Default: :ltr
  ;  :progress-duration (ms)(opt)
  ;   Default: 250
  ;  :style (map)(opt)
  ;  :target (keyword)(opt)
  ;   :blank, :self
  ;  :tooltip-content (metamorphic-content)(opt)
  ;  :tooltip-position (keyword)(opt)
  ;   :left, :right}
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
   (let [button-props (icon-button.prototypes/button-props-prototype button-props)]
        [icon-button button-id button-props])))
