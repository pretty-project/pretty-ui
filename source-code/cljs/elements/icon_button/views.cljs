
(ns elements.icon-button.views
    (:require [elements.element.helpers        :as element.helpers]
              [elements.icon-button.helpers    :as icon-button.helpers]
              [elements.icon-button.presets    :as icon-button.presets]
              [elements.icon-button.prototypes :as icon-button.prototypes]
              [random.api                      :as random]
              [re-frame.api                    :as r]
              [reagent.api                     :as reagent]
              [x.components.api                :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- icon-button-progress
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:progress (percent)(opt)}
  [button-id {:keys [progress] :as button-props}]
  (if progress (let [progress-attributes (icon-button.helpers/progress-attributes button-id button-props)]
                    [:svg.e-icon-button--progress-svg {:view-box "0 0 24 24"}
                                                      [:circle.e-icon-button--progress-circle progress-attributes]])))

(defn- icon-button-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:label (metamorphic-content)(opt)}
  [_ {:keys [label]}]
  (if label [:div.e-icon-button--label {:data-color         :default
                                        :data-font-weight   :extra-bold
                                        :data-text-overflow :ellipsis}
                                       (x.components/content label)]))

(defn- icon-button-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:icon (keyword)
  ;  :icon-color (keyword)
  ;  :icon-family (keyword)}
  [_ {:keys [icon icon-color icon-family]}]
  [:i.e-icon-button--icon {:data-icon-color icon-color :data-icon-family icon-family :data-icon-size :m}
                          icon])

(defn- icon-button-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  [:button.e-icon-button--body (icon-button.helpers/button-body-attributes button-id button-props)
                               [icon-button-progress                       button-id button-props]
                               [icon-button-icon                           button-id button-props]
                               [icon-button-label                          button-id button-props]])

(defn- icon-button-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  [:div.e-icon-button (icon-button.helpers/button-attributes button-id button-props)
                      [icon-button-body                      button-id button-props]])

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
  ;  :border-radius (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;  :class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;   Default: false
  ;  :height (keyword)(opt)
  ;   :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :xxl
  ;  :hover-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :icon (keyword)
  ;  :icon-color (keyword or string)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :inherit
  ;  :icon-family (keyword)(opt)
  ;   :material-icons-filled, :material-icons-outlined
  ;   Default: :material-icons-filled
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :left (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :right (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :top (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;  :keypress (map)(constant)(opt)
  ;   {:key-code (integer)
  ;    :required? (boolean)(opt)
  ;     Default: false}
  ;  :label (metamorphic-content)(opt)
  ;  :marker-color (keyword)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :marker-position (keyword)(opt)
  ;   :tl, :tr, :br, :bl
  ;   Default: :tr
  ;   W/ {:marker-color ...}
  ;  :on-click (metamorphic handler)(opt)
  ;  :on-mouse-over (metamorphic handler)(opt)
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
  ;  :preset (keyword)(opt)
  ;  :progress (percent)(opt)
  ;  :progress-duration (ms)(opt)
  ;   W/ {:progress ...}
  ;  :style (map)(opt)
  ;  :tooltip (metamorphic-content)(opt)
  ;  :width (keyword)(opt)
  ;   :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :xxl}
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
   (let [button-props (element.helpers/apply-preset icon-button.presets/BUTTON-PROPS-PRESETS button-props)
         button-props (icon-button.prototypes/button-props-prototype button-props)]
        [icon-button button-id button-props])))
