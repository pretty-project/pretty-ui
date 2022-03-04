
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.icon-button
    (:require [mid-fruits.candy                           :refer [param return]]
              [x.app-components.api                       :as components]
              [x.app-core.api                             :as a :refer [r]]
              [x.app-elements.engine.api                  :as engine]
              [x.app-elements.element-presets.icon-button :as element-presets.icon-button]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-elements.element-presets.icon-button
(def BUTTON-PROPS-PRESETS element-presets.icon-button/BUTTON-PROPS-PRESETS)



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- button-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) button-props
  ;  {:variant (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :icon-family (keyword)
  ;   :height (keyword)
  ;   :variant (keyword)}
  [{:keys [variant] :as button-props}]
  (merge {:icon-family :material-icons-filled
          :height  :xxl}
         (case variant :filled      {:background-color :primary}
                       :transparent {:color            :primary}
                                    {:background-color :primary
                                     :variant          :filled})
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
  [:button.x-icon-button--body (engine/clickable-body-attributes button-id button-props)
                               [icon-button-icon                 button-id button-props]
                               [icon-button-label                button-id button-props]
                               [engine/element-badge             button-id button-props]])

(defn- icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  [:div.x-icon-button (engine/element-attributes   button-id button-props)
                      [icon-button-body            button-id button-props]
                      [engine/element-info-tooltip button-id button-props]])

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
  ;    :highlight, :muted, :primary, :secondary, :success, :warning
  ;    Default: :primary
  ;    Only w/ {:variant :filled}
  ;   :class (keyword or keywords in vector)(opt)
  ;   :color (keyword)(opt)
  ;    :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;    Default: :primary
  ;    Only w/ {:variant :transparent}
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :icon (keyword)
  ;   :icon-family (keyword)(opt)
  ;    :material-icons-filled, :material-icons-outlined
  ;    Default: :material-icons-filled
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
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
  ;    XXX#8672
  ;   :style (map)(opt)
  ;   :tooltip (metamorphic-content)(opt)
  ;   :variant (keyword)(opt)
  ;    :filled, :placeholder, :transparent
  ;    Default: :filled}
  ;
  ; @usage
  ;  [elements/icon-button {...}]
  ;
  ; @usage
  ;  [elements/icon-button :my-button {...}]
  ;
  ; @usage
  ;  [elements/icon-button {:auto-focus? true :keypress {:key-code 13} :on-click [:do-something!]}]
  ([button-props]
   [element (a/id) button-props])

  ([button-id {:keys [keypress] :as button-props}]
   (let [button-props (engine/apply-preset    BUTTON-PROPS-PRESETS button-props)
         button-props (button-props-prototype button-props)]
        (if keypress [stated-element button-id button-props]
                     [icon-button    button-id button-props]))))
