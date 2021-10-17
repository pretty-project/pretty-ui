
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.23
; Description:
; Version: v3.0.8
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-specials.position-signal
    (:require [mid-fruits.keyword    :as keyword]
              [x.app-components.api  :as components]
              [x.app-core.api        :as a :refer [r]]
              [x.app-environment.api :as environment]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;
  ; @return (map)
  ;  {:component-props (map)
  ;   {:position-signal (integer)}}
  [db [_ component-id context-props]]
  (let [position-signal (r environment/get-element-position db component-id)]
       (assoc-in context-props [:component-props :position-signal] position-signal)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::init-component!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  (fn [_ [_ component-id]]
      [:x.app-environment.position-handler/add-position-listener! component-id]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- position-signal-component
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:component-props (map)
  ;   {:position-signal (integer)}}
  ;
  ; @return (hiccup)
  [component-id {:keys [component component-props]}]
  [:div.x-position-signal {:id (keyword/to-dom-value component-id)}
    [component component-id component-props]])

(defn view
  ; @param (keyword)(opt) component-id
  ; @param (map) context-props
  ;  {:component (component)
  ;   :component-props (map)(opt)}
  ;
  ; @usage
  ;  (defn my-component [component-id {:keys [position-signal] :as component-props}])
  ;  [components/position-signal :my-component {:component #'my-component}]
  ;
  ; @return (component)
  ([context-props]
   [view nil context-props])

  ([component-id context-props]
   (let [component-id (a/id component-id)]
        [components/stated
         component-id
         {:component   position-signal-component
          :initializer [::init-component! component-id]
          :subscriber  [::get-view-props  component-id context-props]}])))
