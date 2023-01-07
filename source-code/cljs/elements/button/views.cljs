
(ns elements.button.views
    (:require [elements.button.helpers    :as button.helpers]
              [elements.button.presets    :as button.presets]
              [elements.button.prototypes :as button.prototypes]
              [elements.element.helpers   :as element.helpers]
              [reagent.api                :as reagent]
              [random.api                 :as random]
              [x.components.api           :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- button-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:label (metamorphic-content)(opt)}
  [_ {:keys [label]}]
  (if label [:div.e-button--label (x.components/content label)]))

(defn- button-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:icon (keyword)(opt)}
  [button-id {:keys [icon] :as button-props}]
  [:i.e-button--icon (button.helpers/button-icon-attributes button-id button-props)
                     icon])

(defn- button-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:icon (keyword)(opt)
  ;  :icon-position (keyword)(opt)
  ;  :label (string)(opt)}
  [button-id {:keys [icon icon-position label] :as button-props}]
  ; XXX#4519 (resources/public/css/elements/style.css)
  [:button.e-button--body (button.helpers/button-body-attributes button-id button-props)
                          (if icon (case icon-position :left  [button-icon button-id button-props]
                                                       :right [:<>]))
                                                       ;:right (if label [:div.e-button--icon-placeholder])
                          [button-label button-id button-props]
                          (if icon (case icon-position :left  [:<>]
                                                       ;:left  (if label [:div.e-button--icon-placeholder])
                                                       :right [button-icon button-id button-props]))])

(defn- button-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  [:div.e-button (button.helpers/button-attributes button-id button-props)
                 [button-body                      button-id button-props]])

(defn- button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (button.helpers/button-did-mount    button-id button-props))
                       :component-will-unmount (fn [_ _] (button.helpers/button-will-unmount button-id button-props))
                       :component-did-update   (fn [%]   (button.helpers/button-did-update   button-id %))
                       :reagent-render         (fn [_ button-props] [button-structure button-id button-props])}))

(defn element
  ; XXX#0714
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
  ;  :border-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :border-radius (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;  :class (keyword or keywords in vector)(opt)
  ;  :color (keyword or string)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :inherit
  ;  :disabled? (boolean)(opt)
  ;   Default: false
  ;  :fill-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :font-size (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  ;   Default: :s
  ;  :font-weight (keyword)(opt)
  ;   :bold, :extra-bold, :inherit, :normal
  ;   Default: :bold
  ;  :horizontal-align (keyword)(opt)
  ;   :center, :left, :right
  ;   Default: :center
  ;  :hover-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :icon (keyword)(opt)
  ;  :icon-color (keyword or string)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :inherit
  ;  :icon-family (keyword)(opt)
  ;   :material-icons-filled, :material-icons-outlined
  ;   Default: :material-icons-filled
  ;  :icon-position (keyword)(opt)
  ;   :left, :right
  ;   Default: :left
  ;  :icon-size (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  ;   Default: :s
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :left (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :right (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :top (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;  :keypress (map)(opt)
  ;   {:key-code (integer)
  ;    :required? (boolean)(opt)
  ;     Default: false}
  ;  :label (metamorphic-content)(opt)
  ;  :line-height (keyword)(opt)
  ;   :block, :inherit, :native, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :block
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
  ;  :style (map)(opt)}
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
   (let [button-props (element.helpers/apply-preset button.presets/BUTTON-PROPS-PRESETS button-props)
         button-props (button.prototypes/button-props-prototype button-props)]
        [button button-id button-props])))
