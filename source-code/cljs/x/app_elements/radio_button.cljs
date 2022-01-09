
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.20
; Description:
; Version: v0.7.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.radio-button
    (:require [mid-fruits.candy          :refer [param return]]
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
  ;  {:border-color (keyword)
  ;   :get-label-f (function)
  ;   :get-value-f (function)
  ;   :layout (keyword)
  ;   :options-path (item-path vector)
  ;   :value-path (item-path vector)}
  [button-id button-props]
  (merge {:border-color :primary
          :layout       :row
          :options-path (engine/default-options-path button-id)
          :value-path   (engine/default-value-path   button-id)
          :get-label-f  return
          :get-value-f  return}
         (param button-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-radio-button-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ;
  ; @return (map)
  [db [_ button-id]]
  (merge (r engine/get-element-props    db button-id)
         (r engine/get-selectable-props db button-id)))

(a/reg-sub :elements/get-radio-button-props get-radio-button-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- radio-button-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:label (metamorphic-content)
  ;   :required? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [_ {:keys [label required?]}]
  [:div.x-radio-button--label [components/content {:content label}]
                              (if required? [:span.x-input--label-asterisk "*"])])

(defn- radio-button-option
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; @param (*) option
  ;
  ; @return (hiccup)
  [button-id {:keys [get-label-f] :as button-props} option]
  (let [option-label (get-label-f option)]
       [:button.x-radio-button--option (engine/selectable-option-attributes button-id button-props option)
                                       [:div.x-radio-button--option-button]
                                       [:div.x-radio-button--option-label [components/content {:content option-label}]]]))

(defn- radio-button-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:options (vector)}
  ;
  ; @return (hiccup)
  [button-id {:keys [options] :as button-props}]
  (vec (reduce #(conj %1 [radio-button-option button-id button-props %2])
                [:div.x-radio-button--options]
                (param options))))

(defn- radio-button-unselect-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:unselectable? (boolean)(opt)}
  ;
  ; @return (component)
  [button-id {:keys [unselectable?] :as button-props}]
  (if unselectable? [:button.x-radio-button--unselect-button (engine/selectable-unselect-attributes button-id button-props)]))

(defn- radio-button-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (hiccup)
  [button-id button-props]
  [:div.x-radio-button--header [radio-button-unselect-button button-id button-props]
                               [radio-button-label           button-id button-props]])

(defn- radio-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (hiccup)
  [button-id button-props]
  [:div.x-radio-button (engine/selectable-attributes button-id button-props)
                       [radio-button-header          button-id button-props]
                       [engine/element-helper        button-id button-props]
                       [radio-button-options         button-id button-props]])

(defn element
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:border-color (keyword)(opt)
  ;    :default, :muted, :primary, :secondary, :success, :warning
  ;    Default: :primary
  ;   :class (keyword or keywords in vector)(opt)
  ;   :default-value (*)(constant)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :disabler (subscription-vector)(opt)
  ;   :form-id (keyword)(opt)
  ;   :get-label-f (function)(constant)(opt)
  ;    Default: return
  ;   :get-value-f (function)(opt)
  ;    Default: return
  ;   :helper (metamorphic-content)(opt)
  ;    TODO ...
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
  ;   :initial-options (vector)(constant)(opt)
  ;   :initial-value (*)(constant)(opt)
  ;   :label (metamorphic-content)
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :row
  ;   :on-select (metamorphic-event)(constant)(opt)
  ;   :options-path (item-path vector)(constant)(opt)
  ;   :required? (boolean)(constant)(opt)
  ;    Default: false
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
   [element (a/id) button-props])

  ([button-id button-props]
   (let [button-props (button-props-prototype button-id button-props)]
        [engine/stated-element button-id
                               {:render-f      #'radio-button
                                :element-props button-props
                                :initializer   [:elements/init-selectable!       button-id]
                                :subscriber    [:elements/get-radio-button-props button-id]}])))
