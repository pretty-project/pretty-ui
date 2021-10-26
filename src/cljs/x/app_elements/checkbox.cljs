
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.21
; Description:
; Version: v0.5.4
; Compatibility: x4.4.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.checkbox
    (:require [mid-fruits.candy          :refer [param]]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- checkbox-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :layout (keyword)
  ;   :font-size (keyword)}
  [checkbox-id checkbox-props]
  (merge {:color     :primary
          :font-size :s
          :layout    :row
          :value-path (engine/default-value-path checkbox-id)}
         (param checkbox-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ;
  ; @return (map)
  [db [_ checkbox-id]]
  (merge (r engine/get-element-view-props   db checkbox-id)
         (r engine/get-checkable-view-props db checkbox-id)))

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- checkbox-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) view-props
  ;  {:label (metamorphic-content)(opt)
  ;   :required? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [_ {:keys [label required?]}]
  (if (some? label)
      [:div.x-checkbox--label [components/content {:content label}]
                              (if required? "*")]))

(defn- checkbox-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [checkbox-id view-props]
  [:button.x-checkbox--body
   (engine/checkable-body-attributes checkbox-id view-props)
   [:div.x-checkbox--button]
   [checkbox-label checkbox-id view-props]])

(defn- checkbox
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [checkbox-id view-props]
  [:div.x-checkbox
    (engine/checkable-attributes checkbox-id view-props)
    [checkbox-body checkbox-id view-props]])

(defn view
  ; @param (keyword)(opt) checkbox-id
  ; @param (map) checkbox-props
  ;  {:color (keyword)(opt)
  ;    :primary, :secondary, :warning, :success, :muted, :default
  ;    Default: :primary
  ;   :default-value (boolean)(constant)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :disabler (subscription vector)(opt)
  ;   :font-size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;   :helper (metamorphic-content)(opt)
  ;   :initial-value (boolean)(constant)(opt)
  ;   :on-check (metamorphic-event)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :row
  ;   :required? (boolean)(constant)(opt)
  ;    Default: false
  ;   :style (map)(opt)
  ;   :value-path (item-path vector)(constant)(opt)}
  ;
  ; @usage
  ;  [elements/checkbox {...}]
  ;
  ; @usage
  ;  [elements/checkbox :my-checkbox {...}]
  ;
  ; @return (component)
  ([checkbox-props]
   [view nil checkbox-props])

  ([checkbox-id checkbox-props]
   (let [checkbox-id    (a/id   checkbox-id)
         checkbox-props (a/prot checkbox-id checkbox-props checkbox-props-prototype)]
        [engine/container checkbox-id
          {:base-props  checkbox-props
           :component   checkbox
           :initializer [:x.app-elements/init-input! checkbox-id]
           :subscriber  [::get-view-props            checkbox-id]}])))
