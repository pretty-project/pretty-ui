
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.21
; Description:
; Version: v2.9.2
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.surface
    (:require [mid-fruits.candy         :refer [param return]]
              [mid-fruits.keyword       :as keyword]
              [x.app-core.api           :as a :refer [r]]
              [x.app-ui.renderer        :as renderer :rename {component renderer}]
              [x.app-ui.surface-layouts :refer [surface-element]]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- surface-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ;
  ; @return (map)
  ;  {:hide-animated? (boolean)
  ;   :reveal-animated? (boolean)
  ;   :trim-content? (boolean)
  ;   :update-animated? (boolean)}
  [db [_ surface-id surface-props]]
  (merge {:hide-animated?   false
          :reveal-animated? false
          :trim-content?    false
          :update-animated? false}
         (param surface-props)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :ui/close-surface!
  ; @param (keyword) surface-id
  (fn [{:keys [db]} [_ surface-id]]
      [:ui/destroy-element! :surface surface-id]))

(a/reg-event-fx
  :ui/render-surface!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ;  {:render-animated? (boolean)(opt)}
  (fn [{:keys [db]} [_ surface-id {:keys [render-animated?] :as surface-props}]]
      (let [close-popups-duration (r renderer/get-visible-elements-destroying-duration db :popups)]
           {:dispatch-later
            [{:ms                     0 :dispatch [:ui/destroy-all-elements! :popups]}
             {:ms close-popups-duration :dispatch [:environment/enable-scroll!]}
             {:ms close-popups-duration :dispatch [:ui/request-rendering-element! :surface surface-id surface-props]}]})))

(a/reg-event-fx
  :ui/set-surface!
  ; @param (keyword)(opt) surface-id
  ; @param (map) surface-props
  ;  {:destructor (metamorphic-event)(opt)
  ;   :hide-animated? (boolean)(opt)
  ;    Default: false
  ;   :horizontal-align (keyword)(opt)
  ;    TODO ... (same as popup)
  ;   :initializer (metamorphic-event)(opt)
  ;   :reveal-animated? (boolean)(opt)
  ;    Default: false
  ;   :trim-content? (boolean)(opt)
  ;    A surface felületéről az X tengelyen túlméretes tartalom elrejtése.
  ;    Default: false
  ;    BUG#9330
  ;    A surface felületén megjelenített {position: sticky;} tulajdonságú
  ;    tartalmak pozícionálása nem kompatibilis a {:trim-content? true}
  ;    tulajdonság használatával!
  ;   :update-animated? (boolean)(opt)
  ;    Default: false
  ;   :view (map)
  ;    {:content (component)
  ;     :content-props (map)(opt)
  ;     :subscriber (subscription-vector)(opt)}}
  ;
  ; @usage
  ;  [:ui/set-surface! {...}]
  ;
  ; @usage
  ;  [:ui/set-surface! :my-surface {...}]
  ;
  ; @usage
  ;  (defn view [surface-id] [:div "My surface"])
  ;  [:ui/set-surface! {:view {:content #'view}}]
  (fn [{:keys [db]} event-vector]
      (let [surface-id    (a/event-vector->second-id   event-vector)
            surface-props (a/event-vector->first-props event-vector)
            surface-props (r surface-props-prototype db surface-id surface-props)]
           [:ui/render-surface! surface-id surface-props])))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (component)
  []
  [renderer :surface {:element               #'surface-element
                      :max-elements-rendered 1
                      :queue-behavior        :push
                      :required?             true
                      :rerender-same?        false}])
