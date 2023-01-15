
(ns elements.button.views
    (:require [elements.button.helpers    :as button.helpers]
              [elements.button.prototypes :as button.prototypes]
              [pretty-css.api             :as pretty-css]
              [re-frame.api               :as r]
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
  (if label [:div.e-button--label {:data-text-overflow :no-wrap}
                                  (x.components/content label)]))

(defn- button-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:icon (keyword)(opt)}
  [button-id {:keys [icon] :as button-props}]
  [:i.e-button--icon (pretty-css/icon-attributes {} button-props)
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
  [:button.e-button--body (button.helpers/button-body-attributes button-id button-props)
                          (if icon (case icon-position :left  [:<> [button-icon  button-id button-props]
                                                                   [button-label button-id button-props]]
                                                       :right [:<> [button-label button-id button-props]
                                                                   [button-icon  button-id button-props]])
                                   [button-label button-id button-props])])

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
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (r/dispatch [:elements.button/button-did-mount    button-id button-props]))
                       :component-will-unmount (fn [_ _] (r/dispatch [:elements.button/button-will-unmount button-id button-props]))
                       :component-did-update   (fn [%]   (r/dispatch [:elements.button/button-did-update   button-id %]))
                       :reagent-render         (fn [_ button-props] [button-structure button-id button-props])}))

(defn element
  ; XXX#0714
  ; Some other items based on the button element and their documentations are linked to here.
  ;
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
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :border-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :class (keyword or keywords in vector)(opt)
  ;  :color (keyword or string)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :inherit
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :font-size (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  ;   Default: :s
  ;  :font-weight (keyword)(opt)
  ;   :inherit, :extra-light, :light, :normal, :medium, :bold, :extra-bold
  ;   Default: :medium
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
  ;   :inherit, :native, :text-block, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :text-block
  ;  :marker-color (keyword)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :marker-position (keyword)(opt)
  ;   :tl, :tr, :br, :bl
  ;  :on-click (metamorphic handler)(opt)
  ;  :on-mouse-over (metamorphic handler)(opt)
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
  ;  :style (map)(opt)
  ;  :tooltip (metamorphic-content)(opt)
  ;  :tooltip-position (keyword)(opt)
  ;   :left, :right}
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
   (let [button-props (button.prototypes/button-props-prototype button-props)]
        [button button-id button-props])))
