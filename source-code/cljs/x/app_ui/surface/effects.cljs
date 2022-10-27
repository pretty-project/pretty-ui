
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.surface.effects
    (:require [re-frame.api                :as r :refer [r]]
              [x.app-ui.renderer           :as renderer]
              [x.app-ui.surface.prototypes :as surface.prototypes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :ui/remove-surface!
  ; @param (keyword) surface-id
  (fn [{:keys [db]} [_ surface-id]]
      [:ui/destroy-element! :surface surface-id]))

(r/reg-event-fx :ui/render-surface-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ;  {:render-animated? (boolean)(opt)}
  (fn [{:keys [db]} [_ surface-id {:keys [render-animated?] :as surface-props}]]
      (let [close-popups-duration (r renderer/get-visible-elements-destroying-duration db :popups)]
           {:dispatch-later [{:ms                     0 :dispatch [:ui/destroy-all-elements! :popups]}
                             {:ms close-popups-duration :dispatch [:environment/enable-scroll!]}
                             {:ms close-popups-duration :dispatch [:ui/request-rendering-element! :surface surface-id surface-props]}]})))

(r/reg-event-fx :ui/render-surface!
  ; @param (keyword)(opt) surface-id
  ; @param (map) surface-props
  ;  {:content (metamorphic-content)
  ;   :on-mount (metamorphic-event)(opt)
  ;   :on-unmount (metamorphic-event)(opt)}
  ;
  ; @usage
  ;  [:ui/render-surface! {...}]
  ;
  ; @usage
  ;  [:ui/render-surface! :my-surface {...}]
  ;
  ; @usage
  ;  (defn my-view [surface-id] [:div "My surface"])
  ;  [:ui/render-surface! {:content #'my-view}]
  [r/event-vector<-id]
  (fn [{:keys [db]} [_ surface-id surface-props]]
      (let [surface-props (surface.prototypes/surface-props-prototype surface-props)]
           [:ui/render-surface-element! surface-id surface-props])))
