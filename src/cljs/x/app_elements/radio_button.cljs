
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.20
; Description:
; Version: v0.7.0
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.radio-button
    (:require [mid-fruits.candy          :refer [param]]
              [mid-fruits.vector         :as vector]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- button-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :font-size (keyword)
  ;   :layout (keyword)}
  [button-id button-props]
  (merge {:color      :primary
          :font-size  :s
          :layout     :row
          :value-path (engine/default-value-path button-id)}
         (param button-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ;
  ; @return (map)
  [db [_ button-id]]
  (merge (r engine/get-element-view-props    db button-id)
         (r engine/get-selectable-view-props db button-id)))

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- radio-button-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) view-props
  ;  {:label (metamorphic-content)
  ;   :required? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [_ {:keys [label required?]}]
  [:div.x-radio-button--label [components/content {:content label}]
                              (if required? "*")])

(defn- radio-button-option
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) view-props
  ; @param (map) option-props
  ;  {:label (metamorphic-content)
  ;   :value (*)}
  ;
  ; @return (hiccup)
  [button-id view-props {:keys [label] :as option-props}]
  [:button.x-radio-button--option
    (engine/selectable-option-attributes button-id view-props option-props)
    [:div.x-radio-button--option-button]
    [:div.x-radio-button--option-label [components/content {:content label}]]])

(defn- radio-button-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) view-props
  ;  {:options (maps in vector)
  ;   [{:label (metamorphic-content)
  ;     :value (*)}]}
  ;
  ; @return (hiccup)
  [button-id {:keys [options] :as view-props}]
  (reduce #(vector/conj-item %1 [radio-button-option button-id view-props %2])
           [:div.x-radio-button--options]
           (param options)))

(defn- radio-button-unselect-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) view-props
  ;  {:unselectable? (boolean)(opt)}
  ;
  ; @return (component)
  [button-id {:keys [unselectable?] :as view-props}]
  (if unselectable?
      [:button.x-radio-button--unselect-button
         (engine/selectable-unselect-attributes button-id view-props)]))

(defn- radio-button-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [button-id view-props]
  [:div.x-radio-button--header
    [radio-button-unselect-button button-id view-props]
    [radio-button-label           button-id view-props]])

(defn- radio-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [button-id view-props]
  [:div.x-radio-button
    (engine/selectable-attributes button-id view-props)
    [radio-button-header  button-id view-props]
    [radio-button-options button-id view-props]])

(defn view
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:class (string or vector)(opt)
  ;   :color (keyword)(opt)
  ;    :primary, :secondary, :warning, :success, :muted, :default
  ;    Default: :primary
  ;   :default-value (*)(constant)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :disabler (subscription vector)(opt)
  ;   :font-size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;   :helper (metamorphic-content)(opt)
  ;   :initial-value (*)(constant)(opt)
  ;   :label (metamorphic-content)
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :row
  ;   :on-select (metamorphic-event)(constant)(opt)
  ;   :options (maps in vector)
  ;    [{:label (metamorphic-content)
  ;      :value (*)}]
  ;   :request-id (keyword)(constant)(opt)
  ;   :required? (boolean)(constant)(opt)
  ;    Default: false
  ;   :status-animation? (boolean)(opt)
  ;    Default: false
  ;    Only w/ {:request-id ...}
  ;   :style (map)(opt)
  ;   :unselectable? (boolean)(opt)
  ;    Default: false
  ;   :value-path (item-path vector)(constant)(opt)}
  ;
  ; @usage
  ;  [elements/radio-button
  ;   {:options [{:value :foo :label "Foo"} {:value :bar :label "Bar"}]}]
  ;
  ; @usage
  ;  [elements/radio-button :my-radio-button {...}]
  ;
  ; @return (component)
  ([button-props]
   [view nil button-props])

  ([button-id button-props]
   (let [button-id    (a/id   button-id)
         button-props (a/prot button-id button-props button-props-prototype)]
        [engine/container button-id
          {:base-props  button-props
           :component   radio-button
           :initializer [:x.app-elements/init-input! button-id]
           :subscriber  [::get-view-props button-id]}])))
