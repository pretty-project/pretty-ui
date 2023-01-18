
(ns elements.icon-button.views
    (:require [elements.icon-button.attributes :as icon-button.attributes]
              [elements.icon-button.prototypes :as icon-button.prototypes]
              [random.api                      :as random]
              [re-frame.api                    :as r]
              [reagent.api                     :as reagent]
              [x.components.api                :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- icon-button-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:icon (keyword)
  ;  :label (metamorphic-content)(opt)
  ;  :progress (percent)(opt)}
  [button-id {:keys [icon label progress] :as button-props}]
  [:div (icon-button.attributes/button-attributes button-id button-props)
        [:button (icon-button.attributes/button-body-attributes button-id button-props)
                 (if progress [:svg {:class :e-icon-button--progress-svg :view-box "0 0 24 24"}
                                    [:circle (icon-button.attributes/progress-attributes button-id button-props)]])
                 (if icon     [:i   (icon-button.attributes/button-icon-attributes  button-id button-props) icon])
                 (if label    [:div (icon-button.attributes/button-label-attributes button-id button-props)
                                    (x.components/content label)])]])

(defn- icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (r/dispatch [:elements.button/button-did-mount    button-id button-props]))
                       :component-will-unmount (fn [_ _] (r/dispatch [:elements.button/button-will-unmount button-id button-props]))
                       :component-did-update   (fn [%]   (r/dispatch [:elements.button/button-did-update   button-id %]))
                       :reagent-render         (fn [_ button-props] [icon-button-structure button-id button-props])}))

(defn element
  ; @param (keyword)(opt) button-id
  ; @param (map) button-props
  ; {:badge-color (keyword)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :primary
  ;   W/ {:badge-content ...}
  ;  :badge-content (metamorphic-content)(opt)
  ;  :badge-position (keyword)(opt)
  ;   :tl, :tr, :br, :bl
  ;   Default: :tr
  ;   W/ {:badge-content ...}
  ;  :border-radius (map)(opt)
  ;   {:tl (keyword)(opt)
  ;    :tr (keyword)(opt)
  ;    :br (keyword)(opt)
  ;    :bl (keyword)(opt)
  ;    :all (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :height (keyword)(opt)
  ;   :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :3xl
  ;  :hover-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :icon (keyword)
  ;  :icon-color (keyword or string)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :inherit
  ;  :icon-family (keyword)(opt)
  ;   :material-symbols-filled, :material-symbols-outlined
  ;   Default: :material-symbols-filled
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :keypress (map)(constant)(opt)
  ;   {:key-code (integer)
  ;    :required? (boolean)(opt)
  ;     Default: false}
  ;  :label (metamorphic-content)(opt)
  ;  :marker-color (keyword)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :marker-position (keyword)(opt)
  ;   :tl, :tr, :br, :bl
  ;  :on-click (metamorphic handler)(opt)
  ;  :on-mouse-over (metamorphic handler)(opt)
  ;  :outdent (map)(opt)
  ;   Same as the :indent property
  ;  :progress (percent)(opt)
  ;  :progress-duration (ms)(opt)
  ;   W/ {:progress ...}
  ;  :style (map)(opt)
  ;  :tooltip-content (metamorphic-content)(opt)
  ;  :tooltip-position (keyword)(opt)
  ;   :left, :right
  ;  :width (keyword)(opt)
  ;   :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :3xl}
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
