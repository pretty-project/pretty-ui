
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.icon-button
    (:require [mid-fruits.candy                          :refer [param return]]
              [x.app-components.api                      :as components]
              [x.app-core.api                            :as a :refer [r]]
              [x.app-elements.badge-handler.views        :as badge-handler.views]
              [x.app-elements.engine.api                 :as engine]
              [x.app-elements.preset-handler.engine      :as preset-handler.engine]
              [x.app-elements.preset-handler.icon-button :as preset-handler.icon-button]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- button-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) button-props
  ;  {:background-color (keyword)(opt)
  ;   :hover-color (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:background-color (keyword)
  ;   :border-radius (keyword)
  ;   :color (keyword)
  ;   :icon-family (keyword)
  ;   :height (keyword)}
  [{:keys [background-color hover-color] :as button-props}]
  (merge {:color            :default
          :icon-family      :material-icons-filled
          :height           :xxl}
         (if background-color {:border-radius :s})
         (if hover-color      {:border-radius :s})
         (param button-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- icon-button-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:label (metamorphic-content)(opt)}
  [_ {:keys [label]}]
  (if label [:div.x-icon-button--label [components/content label]]))

(defn- icon-button-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:icon (keyword)}
  [_ {:keys [icon]}]
  [:i.x-icon-button--icon icon])

(defn- icon-button-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  [:button.x-icon-button--body (engine/clickable-body-attributes  button-id button-props)
                               [icon-button-icon                  button-id button-props]
                               [icon-button-label                 button-id button-props]
                               [badge-handler.views/element-badge button-id button-props]])

(defn- icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id {:keys [tooltip] :as button-props}]
  [:div.x-icon-button (engine/element-attributes   button-id button-props
                        (if tooltip {:data-tooltip (components/content {:content tooltip})}))
                      [icon-button-body            button-id button-props]])

(defn- stated-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  [engine/stated-element button-id
                         {:render-f      #'icon-button
                          :element-props button-props
                          :destructor    [:elements/destruct-clickable! button-id]
                          :initializer   [:elements/init-clickable!     button-id]}])

(defn element
  ; @param (keyword)(opt) button-id
  ; @param (map) button-props
  ;  {:badge-color (keyword)(opt)
  ;    :primary, :secondary, :success, :warning
  ;   :badge-content (metamorphic-content)(opt)
  ;   :background-color (keyword)(opt)
  ;    :highlight, :muted, :none, :primary, :secondary, :success, :warning
  ;    Default: :none
  ;   :border-color (keyword)(opt)
  ;    :highlight, :muted, :none, :primary, :secondary, :success, :warning
  ;    Default: :none
  ;   :border-radius (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl
  ;    Default: :xs
  ;   :class (keyword or keywords in vector)(opt)
  ;   :color (keyword)(opt)
  ;    :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;    Default: :default
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :hover-color (keyword)(opt)
  ;    :highlight, :muted, :none, :primary, :secondary, :success, :warning
  ;    Default: :none
  ;   :icon (keyword)
  ;   :icon-family (keyword)(opt)
  ;    :material-icons-filled, :material-icons-outlined
  ;    Default: :material-icons-filled
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :keypress (map)(constant)(opt)
  ;    {:key-code (integer)
  ;     :required? (boolean)(opt)
  ;      Default: false}
  ;   :height (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :xxl
  ;   :label (metamorphic-content)(opt)
  ;   :on-click (metamorphic handler)(opt)
  ;   :preset (keyword)(opt)
  ;   :style (map)(opt)
  ;   :variant (keyword)(opt)
  ;    :placeholder}
  ;
  ; @usage
  ;  [elements/icon-button {...}]
  ;
  ; @usage
  ;  [elements/icon-button :my-button {...}]
  ;
  ; @usage
  ;  [elements/icon-button {:auto-focus? true :keypress {:key-code 13} :on-click [:my-event]}]
  ([button-props]
   [element (a/id) button-props])

  ([button-id {:keys [keypress] :as button-props}]
   (let [button-props (preset-handler.engine/apply-preset preset-handler.icon-button/BUTTON-PROPS-PRESETS button-props)
         button-props (button-props-prototype button-props)]
        (if keypress [stated-element button-id button-props]
                     [icon-button    button-id button-props]))))
