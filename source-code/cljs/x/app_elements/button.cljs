
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.16
; Description:
; Version: v0.8.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.button
    (:require [mid-fruits.candy          :refer [param return]]
              [mid-fruits.map            :as map]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]
              [x.app-elements.button-presets :as button-presets]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def BUTTON-PROPS-PRESETS button-presets/BUTTON-PROPS-PRESETS)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- button-props->ignore-button-label?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) button-props
  ;  {:content (metamorphic-content)(opt)
  ;   :layout (keyword)(opt)}
  ;
  ; @return (boolean)
  [{:keys [content layout variant]}]
               ; XXX#0523
  (boolean (or (nil? content)
               (and (= layout  :icon-button)
                    (= variant :outlined)))))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- button-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) button-props
  ;  {:icon (keyword)(opt)
  ;   :keypress (map)(opt)}
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :font-size (keyword)
  ;   :horizontal-align (keyword)
  ;   :icon-family (keyword)
  ;   :layout (keyword)
  ;   :targetable? (boolean)
  ;   :variant (keyword)}
  [{:keys [icon keypress label layout] :as button-props}]
  (merge {:color   :primary
          :layout  :row
          :variant :outlined}
         (if (not= layout :icon-button)
             {:font-size        :s
              :horizontal-align :center})
         (if (some? keypress)
             {:targetable? true})
         (if (some? icon)
             {:icon-family :material-icons-filled})
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
  ;
  ; @return (hiccup)
  [_ button-props]
  (if-not (button-props->ignore-button-label? button-props)
          (let [content-props (components/extended-props->content-props button-props)]
               ; XXX#0523
               [:div.x-button--label [components/content content-props]])))

(defn- button-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:icon (keyword)(opt)}
  ;
  ; @return (component or nil)
  [_ {:keys [icon]}]
  [:i.x-button--icon icon])

(defn- button-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:icon (keyword)(opt)}
  ;
  ; @return (hiccup)
  [button-id {:keys [icon] :as button-props}]
  [:button.x-button--body (engine/clickable-body-attributes button-id button-props)
                          (if (some? icon)
                              [button-icon button-id button-props])
                          [button-label button-id button-props]
                          (if (some? icon)
                              [:div.x-button--icon-placeholder])
                          [engine/element-badge button-id button-props]])

(defn- button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (hiccup)
  [button-id button-props]
  [:div.x-button (engine/element-attributes   button-id button-props)
                 [button-body                 button-id button-props]
                 [engine/element-info-tooltip button-id button-props]])

(defn element
  ; @param (keyword)(opt) button-id
  ; @param (map) button-props
  ;  XXX#0714
  ;  {:badge-color (keyword)(opt)
  ;    :primary, :secondary, :warning, :success
  ;   :class (string or vector)(opt)
  ;   :color (keyword)(opt)
  ;    :primary, :secondary, :warning, :success, :muted, :default, :invert
  ;    Default: :primary
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :disabler (subscription vector)(opt)
  ;   :font-size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;    Only w/ {:layout :fit} or {:layout :row}
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
  ;    Not w/ {:layout :icon-button :variant :outlined}
  ;   :layout (keyword)(opt)
  ;    :fit, :icon-button, :row
  ;    Default: :row
  ;   :on-click (metamorphic handler)(opt)
  ;   :preset (keyword)(opt)
  ;    XXX#8671
  ;   :style (map)(opt)
  ;   :tooltip (metamorphic-content)(opt)
  ;   :variant (keyword)(opt)
  ;    :outlined, :filled, :transparent, :placeholder
  ;    Default: :outlined}
  ;
  ; @usage
  ;  [elements/button {...}]
  ;
  ; @usage
  ;  [elements/button :my-button {...}]
  ;
  ; @usage
  ;  [elements/button {:auto-focus? true :keypress {:key-code 13} :on-click [:do-something!]}]
  ;
  ; @return (hiccup)
  ([button-props]
   [element (a/id) button-props])

  ([button-id button-props]
   (let [button-props (engine/apply-preset BUTTON-PROPS-PRESETS button-props)
         button-props (a/prot button-props button-props-prototype)]
        [engine/stated-element button-id
                               {:component     #'button
                                :element-props button-props
                                :destructor    [:elements/destruct-clickable! button-id]
                                :initializer   [:elements/init-clickable!     button-id]}])))
