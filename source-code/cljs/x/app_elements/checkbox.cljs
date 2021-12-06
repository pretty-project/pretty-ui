
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.21
; Description:
; Version: v0.5.8
; Compatibility: x4.4.8



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
  ;   :indent (keyword)
  ;   :layout (keyword)}
  [checkbox-id checkbox-props]
  (merge {:color  :primary
          :indent :left
          :layout :row
          :value-path (engine/default-value-path checkbox-id)}
         (param checkbox-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-checkbox-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ;
  ; @return (map)
  [db [_ checkbox-id]]
  (merge (r engine/get-element-props   db checkbox-id)
         (r engine/get-checkable-props db checkbox-id)))

(a/reg-sub :elements/get-checkbox-props get-checkbox-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- checkbox-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ;  {:label (metamorphic-content)(opt)
  ;   :required? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [_ {:keys [label required?]}]
  (if (some? label)
      [:div.x-checkbox--label [components/content {:content label}]
                              (if (boolean required?)
                                  [:span.x-input--label-asterisk "*"])]))

(defn- checkbox-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ;
  ; @return (hiccup)
  [checkbox-id checkbox-props]
  [:button.x-checkbox--body (engine/checkable-body-attributes checkbox-id checkbox-props)
                            [:div.x-checkbox--button]
                            [checkbox-label checkbox-id checkbox-props]])

(defn- checkbox
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ;
  ; @return (hiccup)
  [checkbox-id checkbox-props]
  [:div.x-checkbox (engine/checkable-attributes checkbox-id checkbox-props)
                   [checkbox-body               checkbox-id checkbox-props]
                   [engine/element-helper       checkbox-id checkbox-props]])

(defn element
  ; @param (keyword)(opt) checkbox-id
  ; @param (map) checkbox-props
  ;  {:color (keyword)(opt)
  ;    :primary, :secondary, :warning, :success, :muted, :default
  ;    Default: :primary
  ;   :default-value (boolean)(constant)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :disabler (subscription vector)(opt)
  ;   :form-id (keyword)(opt)
  ;   :helper (metamorphic-content)(opt)
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :left
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
   [element (a/id) checkbox-props])

  ([checkbox-id checkbox-props]
   (let [checkbox-props (a/prot checkbox-id checkbox-props checkbox-props-prototype)]
        [engine/stated-element checkbox-id
                               {:component     #'checkbox
                                :element-props checkbox-props
                                :initializer   [:elements/init-input!        checkbox-id]
                                :subscriber    [:elements/get-checkbox-props checkbox-id]}])))
