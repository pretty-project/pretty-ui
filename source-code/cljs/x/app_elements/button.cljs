
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.16
; Description:
; Version: v0.8.2
; Compatibility: x4.4.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.button
    (:require [mid-fruits.candy          :refer [param return]]
              [mid-fruits.keyword        :as keyword]
              [mid-fruits.map            :as map]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Presets -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
;  XXX#8671
;
; A {:layout :icon-button} presetek nem tartalmazzák a :tooltip és :label tulajdonságokat,
; mert az a felhasználás helyén dől el, hogy egy ikon gomb felirattal vagy anélkül jelenik meg.
(def BUTTON-PROPS-PRESETS
     {:accept-button       {:color   :primary
                            :label   :accept!
                            :layout  :row
                            :variant :transparent}
      :action-button       {:color   :primary
                            :layout  :row
                            :variant :transparent}
      :add-icon-button     {:color   :primary
                            :icon    :add
                            :layout  :icon-button
                            :variant :transparent}
      :apps-icon-button    {:color   :default
                            :icon    :apps
                            :layout  :icon-button
                            :variant :transparent}
      :archive-icon-button {:color   :default
                            :icon    :inventory_2
                            :icon-family :material-icons-outlined
                            :layout  :icon-button
                            :variant :transparent}
      :back-button         {:color   :default
                            :icon    :arrow_back
                            :layout  :row
                            :variant :transparent}
      :back-icon-button    {:color   :default
                            :icon    :arrow_back
                            :layout  :icon-button
                            :variant :transparent}
      :cancel-button       {:color   :default
                            :label   :cancel!
                            :layout  :row
                            :variant :transparent}
      :cancel-icon-button  {:color   :default
                            :icon    :close
                            :layout  :icon-button
                            :variant :transparent}
      :close-button        {:label   :close!
                            :layout  :row
                            :variant :transparent}
      :close-icon-button   {:color   :default
                            :icon    :close
                            :layout  :icon-button
                            :variant :transparent}
      :default-icon-button {:color   :default
                            :layout  :icon-button
                            :variant :transparent}
      :default-button      {:color   :default
                            :horizontal-align :left
                            :layout  :row
                            :variant :transparent}
      :delete-button       {:color   :warning
                            :label   :delete!
                            :layout  :row
                            :variant :transparent}
      :delete-icon-button  {:color   :warning
                            :icon    :delete_outline
                            :layout  :icon-button
                            :variant :transparent}
      :duplicate-icon-button {:color   :default
                              :icon    :content_copy
                              :layout  :icon-button
                              :variant :transparent}
      :filters-icon-button  {:color   :default
                             :icon    :filter_alt
                             :icon-family :material-icons-outlined
                             :layout  :icon-button
                             :variant :transparent}
      :forward-icon-button {:color   :default
                            :icon    :arrow_forward
                            :layout  :icon-button
                            :variant :transparent}
      :help-button         {:color   :default
                            :horizontal-align :left
                            :icon    :help_outline
                            :label   :help
                            :layout  :row
                            :variant :transparent}
      :home-icon-button    {:color   :default
                            :icon    :home
                            :layout  :icon-button
                            :variant :transparent}
      :language-button     {:color   :default
                            :horizontal-align :left
                            :icon    :translate
                            :label   :language
                            :layout  :row
                            :variant :transparent}
      :logout-button       {:color   :warning
                            :horizontal-align :left
                            :icon    :logout
                            :label   :logout!
                            :layout  :row
                            :variant :transparent}
      :menu-icon-button    {:color   :default
                            :icon    :more_vert
                            :layout  :icon-button
                            :variant :transparent}
      :more-options-button {:color   :default
                            :horizontal-align :left
                            :icon    :list
                            :label   :more-options
                            :layout  :row
                            :variant :transparent}
      :next-icon-button    {:color   :default
                            :icon    :arrow_forward_ios
                            :layout  :icon-button
                            :variant :transparent}
      :prev-icon-button    {:color   :default
                            :icon    :arrow_back_ios
                            :layout  :icon-button
                            :variant :transparent}
      :primary-button      {:color   :primary
                            :horizontal-align :left
                            :layout  :row
                            :variant :transparent}
      :save-button         {:label   :save!
                            :layout  :row
                            :variant :transparent}
      :save-icon-button    {:color   :primary
                            :icon    :save
                            :layout  :icon-button
                            :variant :transparent}
      :search-icon-button  {:color   :default
                            :icon    :search
                            :layout  :icon-button
                            :variant :transparent}
      :secondary-button    {:color   :secondary
                            :horizontal-align :left
                            :layout  :row
                            :variant :transparent}
      :select-button       {:label   :select
                            :layout  :row
                            :variant :transparent}
      :select-more-icon-button {:color   :default
                                :icon    :radio_button_unchecked
                                :layout  :icon-button
                                :variant :transparent}
      :settings-button     {:color   :default
                            :horizontal-align :left
                            :icon    :settings
                            :label   :settings
                            :layout  :row
                            :variant :transparent}
      :order-by-icon-button    {:color   :default
                                :icon    :sort
                                :layout  :icon-button
                                :variant :transparent}
      :up-icon-button        {:color   :default
                              :icon    :chevron_left
                              :layout  :icon-button
                              :variant :transparent}
      :user-menu-icon-button {:color   :default
                              :icon    :account_circle
                              :layout  :icon-button
                              :variant :transparent}
      :warning-button        {:color   :warning
                              :layout  :row
                              :variant :transparent}})



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
             {:icon-size   :s
              :icon-family :material-icons-filled})
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
  [:i.x-button--icon (keyword/to-dom-value icon)])

(defn- button-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:icon (keyword)(opt)}
  ;
  ; @return (hiccup)
  [button-id {:keys [icon] :as button-props}]
  [:button.x-button--body
    (engine/clickable-body-attributes button-id button-props)
    (if (some? icon)
        [button-icon button-id button-props])
    [button-label button-id button-props]
    (if (some? icon)
        [:div.x-button--icon-placeholder])
    [engine/element-badge        button-id button-props]])

(defn- button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (hiccup)
  [button-id button-props]
  [:div.x-button
    (engine/element-attributes   button-id button-props)
    [button-body                 button-id button-props]
    [engine/element-helper       button-id button-props]
    [engine/element-info-tooltip button-id button-props]])

(defn view
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
  ;   :helper (metamorphic-content)(opt)
  ;   :horizontal-align (keyword)(opt)
  ;    :left, :center, :right
  ;    Default: :center
  ;    Only w/ {:layout :fit} or {:layout :row}
  ;   :icon (keyword)(opt)
  ;   :icon-family (keyword)(opt)
  ;    :material-icons-filled, :material-icons-outlined
  ;    Default: :material-icons-filled
  ;    Only w/ {:icon ...}
  ;   :icon-size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;    Only w/ {:icon ...}
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
   [view nil button-props])

  ([button-id button-props]
   (let [button-id    (a/id button-id)
         button-props (engine/apply-preset BUTTON-PROPS-PRESETS button-props)
         button-props (a/prot button-props button-props-prototype)]
        [engine/stated-element button-id
          {:component     #'button
           :element-props button-props
           :destructor    [:elements/destruct-clickable! button-id]
           :initializer   [:elements/init-clickable!     button-id]}])))
