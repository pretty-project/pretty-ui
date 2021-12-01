
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.23
; Description:
; Version: v3.2.0
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.position-signal
    (:require [x.app-components.api  :as components]
              [x.app-core.api        :as a :refer [r]]
              [x.app-environment.api :as environment]))



;; -- Usage -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  (defn my-component [component-id {:keys [position-signal] :as component-props}])
;  [tools/position-signal :my-component {:component #'my-component}]



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-context-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;
  ; @return (map)
  ;  {:component-props (map)
  ;   {:position-signal (integer)}}
  [db [_ component-id context-props]]
  (let [position-signal (r environment/get-element-position db component-id)]
       (assoc-in context-props [:component-props :position-signal] position-signal)))

(a/reg-sub ::get-context-props get-context-props)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :tools/init-position-signal!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  (fn [_ [_ component-id]]
      [:environment/add-element-position-listener! component-id]))



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
  [:div.x-position-signal {:id (a/dom-value component-id)}
                          [component component-id component-props]])

(defn component
  ; @param (keyword)(opt) component-id
  ; @param (map) context-props
  ;  {:component (component)
  ;   :component-props (map)(opt)}
  ;
  ; @usage
  ;  (defn my-component [component-id {:keys [position-signal] :as component-props}])
  ;  [tools/position-signal :my-component {:component #'my-component}]
  ;
  ; @return (component)
  ([context-props]
   [component (a/id) context-props])

  ([component-id context-props]
   [components/stated component-id
                      {:component   position-signal-component
                       :initializer [:tools/init-position-signal! component-id]
                       :subscriber  [::get-context-props          component-id context-props]}]))
