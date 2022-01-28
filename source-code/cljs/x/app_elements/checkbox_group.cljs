
; WARNING! NOT TESTED! DO NOT USE!

;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.12.07
; Description:
; Version: v0.2.2
; Compatibility:



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.checkbox-group
    (:require [mid-fruits.candy          :refer [param return]]
              [mid-fruits.vector         :as vector]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn checkbox-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) checkbox-props
  ;
  ; @return (map)
  [checkbox-props]
  (merge {}
         (param checkbox-props)))

(defn group-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  ;  {:border-color (keyword)
  ;   :get-label-f (function)
  ;   :get-value-f (function)
  ;   :layout (keyword)
  ;   :options-path (item-path vector)
  ;   :value-path (item-path-vector)}
  [group-id group-props]
  (merge {:border-color :primary
          :layout       :row
          :options-path (engine/default-options-path group-id)
          :value-path   (engine/default-value-path   group-id)
          :get-label-f return
          :get-value-f return}
         (param group-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-checkbox-group-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ;
  ; @return (map)
  [db [_ group-id]]
  (merge (r engine/get-element-props     db group-id)
         (r engine/get-collectable-props db group-id)))

(a/reg-sub :elements/get-checkbox-group-props get-checkbox-group-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- checkbox-group-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;  {:label (metamorphic-content)
  ;   :required? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [_ {:keys [label required?]}]
  [:div.x-checkbox-group--label [components/content label]
                                (if required? [:span.x-input--label-asterisk "*"])])

(defn- checkbox-group-option
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (*) option
  ;
  ; @return (hiccup)
  [group-id {:keys [get-label-f] :as group-props} option]
  (let [option-label (get-label-f option)]
       [:button.x-checkbox-group--option (engine/collectable-option-attributes group-id group-props option)
                                         [:div.x-checkbox-group--option-button]
                                         [:div.x-checkbox-group--option-label [components/content option-label]]]))

(defn- checkbox-group-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;  {:options (vector)}
  ;
  ; @return (hiccup)
  [group-id {:keys [options] :as group-props}]
  (reduce #(conj %1 [checkbox-group-option group-id group-props %2])
           [:div.x-checkbox-group--options]
           (param options)))

(defn- checkbox-group-check-all-options-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (component)
  [group-id {:keys [] :as group-props}]
  (if false [:button.x-checkbox-group--check-all-options-button]))
              ;(engine/...-attributes group-id group-props)

(defn- checkbox-group-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (hiccup)
  [group-id group-props]
  [:div.x-checkbox-group--header [checkbox-group-check-all-options-button group-id group-props]
                                 [checkbox-group-label                    group-id group-props]])

(defn- checkbox-group
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (hiccup)
  [group-id group-props]
  [:div.x-checkbox-group (engine/element-attributes group-id group-props)
                         (str group-props)
                         [checkbox-group-header     group-id group-props]
                         [checkbox-group-options    group-id group-props]])

(defn element
  ; @param (keyword)(opt) group-id
  ; @param (map) group-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :border-color (keyword)(opt)
  ;    :default, :muted, :primary, :secondary, :success, :warning
  ;    Default: :primary
  ;   :default-value (*)(constant)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :form-id (keyword)(opt)
  ;   :get-label-f (function)(constant)(opt)
  ;    Default: return
  ;   :get-value-f (function)(opt)
  ;    Default: return
  ;   :helper (metamorphic-content)(opt)
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
  ;   :initial-options (vector)(constant)(opt)
  ;   :initial-value (*)(constant)(opt)
  ;   :label (metamorphic-content)
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :row
  ;   :on-check (metamorphic-event)(constant)(opt)
  ;   :on-uncheck (metamorphic-event)(constant)(opt)
  ;   :options-path (item-path vector)(constant)(opt)
  ;   :required? (boolean)(constant)(opt)
  ;    Default: false
  ;   :style (map)(opt)
  ;   :value-path (item-path vector)(constant)(opt)}
  ;
  ; @usage
  ;  [elements/checkbox-group {...}]
  ;
  ; @usage
  ;  [elements/checkbox-group :my-checkbox-group {...}]
  ;
  ; @return (component)
  ([group-props]
   [element (a/id) group-props])

  ([group-id group-props]
   (let [group-props (group-props-prototype group-id group-props)]
        [engine/stated-element group-id
                               {:render-f      #'checkbox-group
                                :element-props group-props
                                :initializer   [:elements/init-collectable!        group-id]
                                :subscriber    [:elements/get-checkbox-group-props group-id]}])))
