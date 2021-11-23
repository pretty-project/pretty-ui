
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.07.03
; Description:
; Version: v0.4.4
; Compatibility: x4.4.2



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

; @constant (keyword)
(def DEFAULT-DELETE-BUTTON-ICON :close)



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) chip-props
  ;  {:icon (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :delete-button-icon (keyword)
  ;   :icon-family (keyword)
  ;   :layout (keyword)}
  [{:keys [icon] :as chip-props}]
  (merge {:color              :primary
          :layout             :row
          :delete-button-icon DEFAULT-DELETE-BUTTON-ICON}
         (if (some? icon) {:icon-family :material-icons-filled})
         (param chip-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- chip-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ;  {:icon (keyword)(opt)}
  ;
  ; @return (hiccup)
  [_ {:keys [icon]}]
  (if (some? icon)
      [:i.x-chip--icon (keyword/to-dom-value icon)]))

(defn- chip-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ;  {:label (metamorphic-content)}
  ;
  ; @return (hiccup)
  [_ {:keys [label]}]
  [:div.x-chip--label [components/content {:content label}]])

(defn- chip-delete-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ;  {:delete-button-icon (keyword)
  ;   :on-delete (metamorphic-event)(opt)}
  ;
  ; @return (hiccup)
  [chip-id {:keys [delete-button-icon on-delete] :as chip-props}]
  (if (some? on-delete)
      [:button.x-chip--delete-button
        (engine/deletable-body-attributes chip-id chip-props)
        [:i.x-chip--delete-button-icon (keyword/to-dom-value delete-button-icon)]]))

(defn- chip-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ;
  ; @return (hiccup)
  [chip-id chip-props]
  [:div.x-chip--body
    [chip-icon          chip-id chip-props]
    [chip-label         chip-id chip-props]
    [chip-delete-button chip-id chip-props]])

(defn- chip
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ;
  ; @return (hiccup)
  [chip-id chip-props]
  [:div.x-chip
    (engine/element-attributes chip-id chip-props)
    [chip-body                 chip-id chip-props]])

(defn view
  ; @param (keyword)(opt) chip-id
  ; @param (map) chip-props
  ;  XXX#7701
  ;  {:color (keyword)(opt)
  ;    :primary, :secondary, :warning, :success, :muted, :highlight, :default
  ;    Default: :primary
  ;   :class (string or vector)(opt)
  ;   :delete-button-icon (keyword)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :icon (keyword)(opt)
  ;   :icon-family (keyword)(opt)
  ;    :material-icons-filled, :material-icons-outlined
  ;    Default: :material-icons-filled
  ;    Only w/ {:icon ...}
  ;   :label (metamorphic-content)
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :row
  ;   :on-click (metamorphic-event)(constant)(opt)
  ;    TODO ... A chip elem egésze kattintható
  ;   :on-delete (metamorphic-event)(constant)(opt)
  ;   :style (map)(opt)}
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
        [chip chip-id chip-props])))
