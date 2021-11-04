
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.21
; Description:
; Version: v2.8.8
; Compatibility: x4.4.4



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.surface
    (:require [mid-fruits.candy         :refer [param return]]
              [mid-fruits.keyword       :as keyword]
              [x.app-core.api           :as a :refer [r]]
              [x.app-ui.renderer        :as renderer :refer [view] :rename {view renderer}]
              [x.app-ui.sidebar         :as sidebar]
              [x.app-ui.surface-layouts :refer [view] :rename {view surface-element}]))



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
  [db [surface-id surface-props]]
  (merge {:hide-animated?   false
          :reveal-animated? false
          :trim-content?    false
          :update-animated? false}
         (param surface-props)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-ui/close-surface!
  ; @param (keyword) surface-id
  (fn [{:keys [db]} [_ surface-id]]
      [:x.app-ui/destroy-element! :surface surface-id]))

(a/reg-event-fx
  :x.app-ui/enable-scroll!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-environment.scroll-prohibitor/enable-scroll!])

(a/reg-event-fx
  :x.app-ui/render-surface!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ;  {:render-animated? (boolean)(opt)}
  (fn [{:keys [db]} [_ surface-id {:keys [render-animated?] :as surface-props}]]
      (let [close-popups-duration   (r renderer/get-visible-elements-destroying-duration db :popups)
            sidebar-hiding-duration (r sidebar/get-sidebar-hiding-duration db)
            surface-rendering-delay (+ close-popups-duration sidebar-hiding-duration)]
           {:dispatch-later
            [{:ms                       0 :dispatch [:x.app-ui/destroy-all-elements! :popups]}
             {:ms close-popups-duration   :dispatch [:x.app-ui/enable-scroll!]}
             {:ms close-popups-duration   :dispatch [:x.app-ui/hide-sidebar!]}
             {:ms surface-rendering-delay :dispatch [:x.app-ui/request-rendering-element! :surface surface-id surface-props]}]})))

(a/reg-event-fx
  :x.app-ui/set-surface!
  ; @param (keyword)(opt) surface-id
  ; @param (map) surface-props
  ;  {:content (component)
  ;    XXX#8711
  ;   :content-props (map)(opt)
  ;    XXX#8711
  ;   :destructor (metamorphic-event)(opt)
  ;   :hide-animated? (boolean)(opt)
  ;    Default: false
  ;   :horizontal-align (keyword)(opt)
  ;    TODO ... (same as popup)
  ;   :initializer (metamorphic-event)(opt)
  ;   :reveal-animated? (boolean)(opt)
  ;    Default: false
  ;   :subscriber (subscription vector)(opt)
  ;    XXX#8711
  ;   :trim-content? (boolean)(opt)
  ;    A surface felületéről az X tengelyen túlméretes tartalom elrejtése.
  ;    Default: false
  ;    BUG#9330
  ;    A surface felületén megjelenített {position: sticky;} tulajdonságú
  ;    tartalmak pozícionálása nem kompatibilis a {:trim-content? true}
  ;    tulajdonság használatával!
  ;   :update-animated? (boolean)(opt)
  ;    Default: false}
  ;
  ; @usage
  ;  [:x.app-ui/set-surface! {...}]
  ;
  ; @usage
  ;  [:x.app-ui/set-surface! :my-surface {...}]
  ;
  ; @usage
  ;  (defn view [surface-id] [:div "My surface"])
  ;  [:x.app-ui/set-surface! {:content #'view}]
  (fn [{:keys [db]} event-vector]
      (let [surface-id    (a/event-vector->second-id   event-vector)
            surface-props (a/event-vector->first-props event-vector)
            surface-props (a/sub-prot db [surface-id surface-props] surface-props-prototype)]
           [:x.app-ui/render-surface! surface-id surface-props])))



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
