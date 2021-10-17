
; WARNING! NOT TESTED! DO NOT USE!



;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.24
; Description:
; Version: v0.3.2
; Compatibility:



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.button-group
    (:require [mid-fruits.candy          :refer [param]]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- group-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) group-props
  ;  {:request-id (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:status-animation? (boolean)}
  [{:keys [request-id] :as group-props}]
  (merge {}
         (if (some? request-id) {:status-animation? true})
         (param group-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ;
  ; @return (map)
  [db [_ group-id]]
  (r engine/get-element-view-props db group-id))

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- button-group
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [group-id {:keys [] :as view-props}]
  [:div.x-button-group
    (engine/element-attributes group-id view-props)])

(defn view
  ; @param (keyword)(opt) group-id
  ; @param (map) group-props
  ;  {:buttons (maps in vector)
  ;    [{:icon (keyword) Material icon class
  ;      :on-click (metamorphic handler)(opt)
  ;      :tooltip (metamorphic content)(opt)}]
  ;   :class (string or vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;   :disabler (subscription vector)(opt)
  ;    Default: false
  ;   :request-id (keyword)(constant)(opt)
  ;   :status-animation? (boolean)(opt)
  ;    Default: true
  ;    Only w/ {:request-id ...}
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [elements/button-group {...}]
  ;
  ; @usage
  ;  [elements/button-group :my-button-group {...}]
  ;
  ; @return (component)
  ([group-props]
   [view nil group-props])

  ([group-id group-props]
   (let [group-id    (a/id   group-id)
         group-props (a/prot group-props group-props-prototype)]
        [engine/container group-id
          {:base-props group-props
           :component  button-group
           :subscriber [::get-view-props group-id]}])))
