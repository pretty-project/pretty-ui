
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.button
    (:require [mid-fruits.candy                      :refer [param return]]
              [mid-fruits.map                        :as map]
              [x.app-components.api                  :as components]
              [x.app-core.api                        :as a :refer [r]]
              [x.app-elements.engine.api             :as engine]
              [x.app-elements.element-presets.button :as element-presets.button]
              [x.app-elements.element-presets.engine :as element-presets.engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-elements.element-presets.button
(def BUTTON-PROPS-PRESETS element-presets.button/BUTTON-PROPS-PRESETS)

; x.app-elements.element-presets.engine
(def apply-preset element-presets.engine/apply-preset)



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- button-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) button-props
  ;  {:icon (keyword)(opt)
  ;   :keypress (map)(opt)
  ;   :layout (keyword)(opt)
  ;   :variant (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :font-size (keyword)
  ;   :font-weight (keyword)
  ;   :horizontal-align (keyword)
  ;   :icon-family (keyword)
  ;   :layout (keyword)
  ;   :variant (keyword)}
  [{:keys [icon label layout variant] :as button-props}]
  (merge {:layout :row}
         (if (not= layout :icon-button) {:font-size        :s
                                         :font-weight      :bold
                                         :horizontal-align :center})
         (if icon                   {:icon-family :material-icons-filled})
         (case variant :filled      {:background-color :primary}
                       :transparent {:color            :primary}
                                    {:background-color :primary
                                     :variant          :filled})
         (param button-props)
         ; XXX#0523
         ; A button elemet {:layout :icon-button} beállítással használva,
         ; a {:content ...} tulajdonság neve nehezen értelmezhető,
         ; ezért {:label ...} tulajdonságként kell használni
         (map/rekey-item button-props :label :content)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- button-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:content (metamorphic-content)(opt)}
  [_ {:keys [content]}]
  ; XXX#0523
  (if content [:div.x-button--label [components/content content]]))

(defn- button-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:icon (keyword)(opt)}
  [_ {:keys [icon]}]
  [:i.x-button--icon icon])

(defn- button-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:icon (keyword)(opt)}
  [button-id {:keys [icon] :as button-props}]
  [:button.x-button--body (engine/clickable-body-attributes button-id button-props)
                          (if icon [button-icon button-id button-props])
                          [button-label button-id button-props]
                          (if icon [:div.x-button--icon-placeholder])
                          [engine/element-badge button-id button-props]])

(defn- button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  [:div.x-button (engine/element-attributes   button-id button-props)
                 [button-body                 button-id button-props]
                 [engine/element-info-tooltip button-id button-props]])

(defn- stated-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  [engine/stated-element button-id
                         {:render-f      #'button
                          :element-props button-props
                          :destructor    [:elements/destruct-clickable! button-id]
                          :initializer   [:elements/init-clickable!     button-id]}])

(defn element
  ; @param (keyword)(opt) button-id
  ; @param (map) button-props
  ;  XXX#0714
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
  ;   :font-size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;    Only w/ {:layout :fit} or {:layout :row}
  ;   :font-weight (keyword)(opt)
  ;    :bold, :extra-bold
  ;    Default: :bold
  ;   :horizontal-align (keyword)(opt)
  ;    :left, :center, :right
  ;    Default: :center
  ;    Only w/ {:layout :fit} or {:layout :row}
  ;   :icon (keyword)(opt)
  ;   :icon-family (keyword)(opt)
  ;    :material-icons-filled, :material-icons-outlined
  ;    Default: :material-icons-filled
  ;    Only w/ {:icon ...}
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
  ;   :info-tooltip (metamorphic-content)(opt)
  ;   :keypress (map)(constant)(opt)
  ;    {:key-code (integer)
  ;     :required? (boolean)(opt)
  ;      Default: false}
  ;   :label (metamorphic-content)(opt)
  ;   :layout (keyword)(opt)
  ;    :fit, :icon-button, :row
  ;    Default: :row
  ;   :on-click (metamorphic handler)(opt)
  ;   :preset (keyword)(opt)
  ;    XXX#8671
  ;   :style (map)(opt)
  ;   :tooltip (metamorphic-content)(opt)
  ;   :variant (keyword)(opt)
  ;    :filled, :placeholder, :transparent
  ;    Default: :filled}
  ;
  ; @usage
  ;  [elements/button {...}]
  ;
  ; @usage
  ;  [elements/button :my-button {...}]
  ;
  ; @usage
  ;  [elements/button {:keypress {:key-code 13} :on-click [:do-something!]}]
  ([button-props]
   [element (a/id) button-props])

  ([button-id {:keys [keypress] :as button-props}]
   (let [button-props (apply-preset BUTTON-PROPS-PRESETS button-props)
         button-props (button-props-prototype button-props)]
        (if keypress [stated-element button-id button-props]
                     [button         button-id button-props]))))
