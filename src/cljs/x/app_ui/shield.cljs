
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.21
; Description:
; Version: v0.8.2
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.shield
    (:require [app-fruits.dom    :as dom]
              [x.app-core.api    :as a]
              [x.app-ui.renderer :as renderer]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn shield-hidden?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  []
  (let [shield-element       (dom/get-element-by-id "x-app-shield")
        shield-display-value (dom/get-element-style-value shield-element "display")]
       (= "none" shield-display-value)))

(defn shield-visible?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  []
  (not (shield-hidden?)))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-handled-fx
  :x.app-ui/render-shield-content!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) shield-props
  ;  {:content (*)}
  (fn [[{:keys [content]}]]
      (let [shield-content-element (dom/get-element-by-id "x-app-shield--content")]
           (dom/set-element-content! shield-content-element content))))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-ui/show-shield!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ _]
      {:dispatch-if [(shield-hidden?)
                     [:x.app-environment.element-handler/reveal-animated! "x-app-shield"]]}))

(a/reg-event-fx
  :x.app-ui/hide-shield!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ _]
      {:dispatch-if [(shield-visible?)
                     [:x.app-environment.element-handler/hide-animated!
                      renderer/HIDE-ANIMATION-TIMEOUT "x-app-shield"]]}))

(a/reg-event-fx
  :x.app-ui/set-shield!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword)(opt) shield-id
  ; @param (map) shield-props
  ;  {:content (*)}
  ;
  ; @usage
  ;  [:x.app-ui/set-shield! {...}]
  ;
  ; @usage
  ;  [:x.app-ui/set-shield! :my-shield {...}]
  (fn [_ event-vector]
      (let [shield-id    (a/event-vector->second-id   event-vector)
            shield-props (a/event-vector->first-props event-vector)]
           {:dispatch-later
            [{:ms   0 :dispatch [:x.app-ui/empty-shield!]}
             {:ms  50 :dispatch [:x.app-ui/render-shield-content! shield-props]}
             {:ms 100 :dispatch [:x.app-ui/show-shield!]}]})))

(a/reg-event-fx
  :x.app-ui/remove-shield!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  {:dispatch-later
   [{:ms                               0 :dispatch [:x.app-ui/hide-shield!]}
    {:ms renderer/HIDE-ANIMATION-TIMEOUT :dispatch [:x.app-ui/empty-shield!]}]})

(a/reg-event-fx
  :x.app-ui/empty-shield!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-environment.element-handler/empty-element! "x-app-shield--content"])
