
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.19
; Description:
; Version: v0.9.0
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.switch
    (:require [mid-fruits.candy          :refer [param]]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- switch-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :font-size (keyword)
  ;   :layout (keyword)}
  [switch-id switch-props]
  (merge {:color     :primary
          :font-size :s
          :layout    :row
          :value-path (engine/default-value-path switch-id)}
         (param switch-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ;
  ; @return (map)
  [db [_ switch-id]]
  (merge (r engine/get-element-view-props   db switch-id)
         (r engine/get-checkable-view-props db switch-id)))

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- switch-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) view-props
  ;  {:label (metamorphic-content)
  ;   :required? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [_ {:keys [label required?]}]
  [:div.x-switch--label [components/content {:content label}]
                        (if required? "*")])

(defn- switch-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [switch-id view-props]
  [:button.x-switch--body
    (engine/checkable-body-attributes switch-id view-props)
    [:div.x-switch--track
      [:div.x-switch--thumb]]
    [switch-label switch-id view-props]])

(defn- switch
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [switch-id view-props]
  [:div.x-switch
    (engine/checkable-attributes switch-id view-props)
    [switch-body switch-id view-props]])

(defn view
  ; @param (keyword)(opt) switch-id
  ; @param (map) switch-props
  ;  {:class (string or vector)(opt)
  ;   :color (keyword)(opt)
  ;    :primary, :secondary, :warning, :success, :muted, :default
  ;    Default: :primary
  ;   :default-value (boolean)(constant)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :font-size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;   :helper (metamorphic-content)(opt)
  ;   :initial-value (boolean)(constant)(opt)
  ;   :label (metamorphic-content)
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :row
  ;   :on-check (metamorphic-event)(constant)(opt)
  ;   :on-uncheck (metamorphic-event)(constant)(opt)
  ;   :required? (boolean)(constant)(opt)
  ;    Default: false
  ;   :style (map)(opt)
  ;   :value-path (item-path vector)(constant)(opt)}
  ;
  ; @usage
  ;  [elements/switch {...}]
  ;
  ; @usage
  ;  [elements/switch :my-switch {...}]
  ;
  ; @return (component)
  ([switch-props]
   [view nil switch-props])

  ([switch-id switch-props]
   (let [switch-id    (a/id   switch-id)
         switch-props (a/prot switch-id switch-props switch-props-prototype)]
        [engine/container switch-id
          {:base-props  switch-props
           :component   switch
           :initializer [:x.app-elements/init-input! switch-id]
           :subscriber  [::get-view-props            switch-id]}])))
