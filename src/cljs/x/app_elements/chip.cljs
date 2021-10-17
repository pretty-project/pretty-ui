
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.07.03
; Description:
; Version: v0.4.2
; Compatibility: x4.3.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.chip
    (:require [mid-fruits.candy          :refer [param]]
              [mid-fruits.keyword        :as keyword]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keyword) Material icon class
(def DEFAULT-DELETE-BUTTON-ICON :close)



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) chip-props
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :delete-button-icon (keyword)
  ;   :layout (keyword)
  ;   :variant (keyword)}
  [chip-props]
  (merge {:color              :primary
          :delete-button-icon DEFAULT-DELETE-BUTTON-ICON
          :layout             :row
          :variant            :filled}
         (param chip-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chip-id
  ;
  ; @return (map)
  [db [_ chip-id]]
  (r engine/get-element-view-props db chip-id))

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- chip-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chip-id
  ; @param (map) view-props
  ;  {:icon (keyword)(opt) Material icon class}
  ;
  ; @return (hiccup)
  [_ {:keys [icon]}]
  (if (some? icon)
      [:i.x-chip--icon (keyword/to-dom-value icon)]))

(defn- chip-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chip-id
  ; @param (map) view-props
  ;  {:label (metamorphic-content)}
  ;
  ; @return (hiccup)
  [_ {:keys [label]}]
  [:div.x-chip--label [components/content {:content label}]])

(defn- chip-delete-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chip-id
  ; @param (map) view-props
  ;  {:delete-button-icon (keyword) Material icon class
  ;   :on-delete (metamorphic-event)(opt)}
  ;
  ; @return (hiccup)
  [chip-id {:keys [delete-button-icon on-delete] :as view-props}]
  (if (some? on-delete)
      [:button.x-chip--delete-button
        (engine/deletable-body-attributes chip-id view-props)
        [:i.x-chip--delete-button-icon (keyword/to-dom-value delete-button-icon)]]))

(defn- chip-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chip-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [chip-id view-props]
  [:div.x-chip--body
    [chip-icon          chip-id view-props]
    [chip-label         chip-id view-props]
    [chip-delete-button chip-id view-props]])

(defn- chip
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chip-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [chip-id view-props]
  [:div.x-chip
    (engine/element-attributes chip-id view-props)
    [chip-body                 chip-id view-props]])

(defn view
  ; @param (keyword)(opt) chip-id
  ; @param (map) chip-props
  ;  XXX#7701
  ;  {:color (keyword)(opt)
  ;    :primary, :secondary, :warning, :success, :muted, :highlight, :default
  ;    Default: :primary
  ;   :class (string or vector)(opt)
  ;   :delete-button-icon (keyword)(opt) Material icon class
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :icon (keyword)(opt) Material icon class
  ;   :label (metamorphic-content)
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :row
  ;   :on-click (metamorphic-event)(constant)(opt)
  ;    TODO ... A chip elem egésze kattintható
  ;   :on-delete (metamorphic-event)(constant)(opt)
  ;   :style (map)(opt)
  ;   :variant (keyword)(opt)
  ;    :filled, :outlined
  ;    Default :filled}
  ;
  ; @usage
  ;  [elements/chip {...}]
  ;
  ; @usage
  ;  [elements/chip :my-chip {...}]
  ;
  ; @return (component)
  ([chip-props]
   [view nil chip-props])

  ([chip-id chip-props]
   (let [chip-id    (a/id   chip-id)
         chip-props (a/prot chip-props chip-props-prototype)]
        [engine/container chip-id
          {:base-props chip-props
           :component  chip
           :subscriber [::get-view-props chip-id]}])))
