
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.16
; Description:
; Version: v0.6.4
; Compatibility: x3.9.9



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
(def BUTTON-PROPS-PRESETS
     {:accept-button       {:color   :primary
                            :label   :accept!
                            :layout  :row
                            :variant :transparent}
      :action-button       {:color   :primary
                            :layout  :row
                            :variant :transparent}
      :back-button         {:color   :default
                            :icon    :arrow_back
                            :layout  :row
                            :variant :transparent}
      :back-icon-button    {:color   :default
                            :icon    :arrow_back
                            :layout  :icon-button
                            :tooltip :back!
                            :variant :transparent}
      :cancel-button       {:color   :default
                            :label   :cancel!
                            :layout  :row
                            :variant :transparent}
      :close-button        {:label   :close!
                            :layout  :row
                            :variant :transparent}
      :close-icon-button   {:color   :default
                            :icon    :close
                            :layout  :icon-button
                            :tooltip :close!
                            :variant :transparent}
      :default-icon-button {:color   :default
                            :layout  :icon-button
                            :variant :transparent}
      :default-button      {:color   :default
                            :layout  :row
                            :variant :transparent}
      :delete-button       {:color   :warning
                            :label   :delete!
                            :layout  :row
                            :variant :transparent}
      :home-icon-button    {:color   :default
                            :icon    :apps
                            :layout  :icon-button
                            :tooltip :back-to-home!
                            :variant :transparent}
      :logout-button       {:color   :warning
                            :label   :logout!
                            :layout  :row
                            :variant :transparent}
      :menu-icon-button    {:color   :default
                            :icon    :more_vert
                            :layout  :icon-button
                            :tooltip :app-menu
                            :variant :transparent}
      :save-button         {:label   :save!
                            :layout  :row
                            :variant :transparent}
      :save-icon-button    {:color   :default
                            :icon    :save
                            :layout  :icon-button
                            :tooltip :save!
                            :variant :transparent}
      :select-button       {:label   :select
                            :layout  :row
                            :variant :transparent}})



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-props->ignore-button-label?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
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



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ;
  ; @return (map)
  [db [_ button-id]]
  (r engine/get-element-view-props db button-id))

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- button-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [_ view-props]
  (if-not (view-props->ignore-button-label? view-props)
          (let [content-props (components/extended-props->content-props view-props)]
               ; XXX#0523
               [:div.x-button--label [components/content content-props]])))

(defn- button-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) view-props
  ;  {:icon (keyword)(opt) Material icon class}
  ;
  ; @return (component or nil)
  [_ {:keys [icon]}]
  (if (some? icon)
      [:i.x-button--icon (keyword/to-dom-value icon)]))

(defn- button-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [button-id view-props]
  [:button.x-button--body
    (engine/clickable-body-attributes button-id view-props)
    [button-icon  button-id view-props]
    [button-label button-id view-props]])

(defn- button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [button-id view-props]
  [:div.x-button
    (engine/element-attributes button-id view-props)
    [button-body button-id view-props]])

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
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
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
  ;   :request-id (keyword)(constant)(opt)
  ;   :status-animation? (boolean)(opt)
  ;    Default: false
  ;    Only w/ {:request-id ...}
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
        [engine/container button-id
          {:base-props  button-props
           :component   button
           :destructor  [:x.app-elements/destruct-clickable! button-id]
           :initializer [:x.app-elements/init-clickable!     button-id]
           :subscriber  [::get-view-props                    button-id]}])))
