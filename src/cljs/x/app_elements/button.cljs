
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
      :sort-by-icon-button     {:color   :default
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



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- button-props->ignore-button-label?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) button-props
  ;  {:content (metamorphic-content)(opt)
  ;   :height (keyword)(opt)
  ;   :layout (keyword)(opt)
  ;   :width (keyword)(opt)}
  ;
  ; @return (boolean)
  [{:keys [content height layout variant width]}]
               ; XXX#0523
  (boolean (or (nil? content)
               (and (= layout  :icon-button)
                    (= variant :outlined))
               (and (= layout  :icon-button)
                    (= height  :fit))
               (and (= layout  :icon-button)
                    (= width   :fit)))))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- button-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) button-props
  ;  {:keypress (map)(opt)}
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :font-size (keyword)
  ;   :height (keyword)
  ;   :horizontal-align (keyword)
  ;   :layout (keyword)
  ;   :targetable? (boolean)
  ;   :variant (keyword)
  ;   :width (keyword)}
  [{:keys [keypress label layout] :as button-props}]
  (merge {:color     :primary
          :icon-size :s
          :layout    :row
          :variant   :outlined}
         (if (= layout :icon-button)
             {:height :touch-target
              :width  :touch-target}
             {:font-size        :s
              :horizontal-align :center})
         (if (some? keypress)
             {:targetable? true})
         ; XXX#0523
         ; A button elemet {:layout :icon-button} beállítással használva,
         ; a {:content ...} tulajdonság neve nehezen értelmezhető,
         ; ezért {:label ...} tulajdonságként kell használni
         (map/rekey-value button-props :label :content)))



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
  ;  {:icon (keyword)(opt) Material icon class}
  ;
  ; @return (component or nil)
  [_ {:keys [icon]}]
  [:i.x-button--icon (keyword/to-dom-value icon)])

(defn- button-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:icon (keyword)(opt) Material icon class}
  ;
  ; @return (hiccup)
  [button-id {:keys [icon] :as button-props}]
  [:button.x-button--body
    (engine/clickable-body-attributes button-id button-props)
    (if (some? icon)
        [button-icon button-id button-props])
    [button-label button-id button-props]
    (if (some? icon)
        [:div.x-button--icon-placeholder])])

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
  ;  {:class (string or vector)(opt)
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
  ;   :height (keyword)(opt)
  ;    :fit, :touch-target
  ;    Default: touch-target
  ;    Only w/ {:layout :icon-button}
  ;   :helper (metamorphic-content)(opt)
  ;   :horizontal-align (keyword)(opt)
  ;    :left, :center, :right
  ;    Default: :center
  ;    Only w/ {:layout :fit} or {:layout :row}
  ;   :icon (keyword)(opt) Material icon class
  ;   :icon-size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;   :info-tooltip (metamorphic-content)(opt)
  ;   :keypress (map)(constant)(opt)
  ;    {:key-code (integer)
  ;     :required? (boolean)(opt)
  ;      Default: false}
  ;   :label (metamorphic-content)(opt)
  ;    Not w/ {:layout :icon-button :variant :outlined}
  ;    Not w/ {:layout :icon-button :height  :fit}
  ;    Not w/ {:layout :icon-button :width   :fit}
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
  ;    Default: :outlined
  ;   :width (keyword)(opt)
  ;    :fit, :touch-target
  ;    Default: touch-target
  ;    Only w/ {:layout :icon-button}}
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
           :destructor    [:x.app-elements/destruct-clickable! button-id]
           :initializer   [:x.app-elements/init-clickable!     button-id]}])))
