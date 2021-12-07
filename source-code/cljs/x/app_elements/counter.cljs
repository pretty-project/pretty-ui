
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.21
; Description:
; Version: v0.6.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.counter
    (:require [mid-fruits.candy          :refer [param]]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- counter-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :font-size (keyword)
  ;   :indent (keyword)
  ;   :layout (keyword)}
  [counter-id counter-props]
  (merge {:color     :primary
          :font-size :s
          :indent    :left
          :layout    :row
          :value-path (engine/default-value-path counter-id)}
         (param counter-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-counter-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ;
  ; @return (map)
  [db [_ counter-id]]
  (merge (r engine/get-element-props   db counter-id)
         (r engine/get-countable-props db counter-id)))

(a/reg-sub :elements/get-counter-props get-counter-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- counter-reset-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;  {:resetable? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [counter-id {:keys [resetable?] :as counter-props}]
  (if resetable? [:button.x-counter--reset-button (engine/countable-reset-attributes counter-id counter-props)]))

(defn- counter-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;  {:label (metamorphic-content)(opt)
  ;   :required? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [_ {:keys [label required?]}]
  (if (some? label)
      [:div.x-counter--label [components/content {:content label}]
                             (if required? [:span.x-input--label-asterisk "*"])]))

(defn- counter-increase-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;  {:disabled? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [counter-id counter-props]
  [:button.x-counter--increase-button (engine/countable-increase-attributes counter-id counter-props)])

(defn- counter-decrease-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;  {:disabled? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [counter-id counter-props]
  [:button.x-counter--decrease-button (engine/countable-decrease-attributes counter-id counter-props)])

(defn- counter-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;  {:value (integer)}
  ;
  ; @return (hiccup)
  [counter-id {:keys [value] :as counter-props}]
  [:div.x-counter--body [counter-decrease-button counter-id counter-props]
                        [:div.x-counter--value value]
                        [counter-increase-button counter-id counter-props]
                        [counter-reset-button    counter-id counter-props]
                        [counter-label           counter-id counter-props]])

(defn- counter
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) counter-id
  ; @param (map) counter-props
  ;
  ; @return (hiccup)
  [counter-id counter-props]
  [:div.x-counter (engine/element-attributes counter-id counter-props)
                  [counter-body              counter-id counter-props]
                  [engine/element-helper     counter-id counter-props]])

(defn element
  ; @param (keyword)(opt) counter-id
  ; @param (map) counter-props
  ;  {:color (keyword)(opt)
  ;    :primary, :secondary, :warning, :success, :muted, :default
  ;    Default: :primary
  ;   :default-value (integer)(constant)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :disabler (subscription vector)(opt)
  ;   :form-id (keyword)(opt)
  ;   :helper (metamorphic-content)(opt)
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :left
  ;   :initial-value (integer)(constant)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :row
  ;   :max-value (integer)(opt)
  ;   :min-value (integer)(opt)
  ;   :resetable? (boolean)(opt)
  ;    Default: false
  ;   :required? (boolean)(constant)(opt)
  ;    Default: false
  ;   :style (map)(opt)
  ;   :value-path (item-path vector)(constant)(opt)}
  ;
  ; @usage
  ;  [elements/counter {...}]
  ;
  ; @usage
  ;  [elements/counter :my-counter {...}]
  ;
  ; @return (component)
  ([counter-props]
   [element (a/id) counter-props])

  ([counter-id counter-props]
   (let [counter-props (a/prot counter-id counter-props counter-props-prototype)]
        [engine/stated-element counter-id
                               {:component     #'counter
                                :element-props counter-props
                                :initializer   [:elements/init-input!       counter-id]
                                :subscriber    [:elements/get-counter-props counter-id]}])))
