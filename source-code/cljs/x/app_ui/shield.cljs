
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
  (let [shield-element       (dom/get-element-by-id       "x-app-shield")
        shield-display-value (dom/get-element-style-value shield-element "display")]
       (= "none" shield-display-value)))

(defn shield-visible?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  []
  (let [shield-hidden? (shield-hidden?)]
       (not shield-hidden?)))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- render-shield-content!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) shield-props
  ;  {:content (*)}
  [{:keys [content]}]
  (let [shield-content-element (dom/get-element-by-id "x-app-shield--content")]
       (dom/set-element-content! shield-content-element content)))

(a/reg-fx_ :ui/render-shield-content! render-shield-content!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :ui/show-shield!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ _]
      {:dispatch-if [(shield-hidden?)
                     [:environment/reveal-element-animated! "x-app-shield"]]}))

(a/reg-event-fx
  :ui/hide-shield!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ _]
      {:dispatch-if [(shield-visible?)
                     [:environment/hide-element-animated! renderer/HIDE-ANIMATION-TIMEOUT "x-app-shield"]]}))

(a/reg-event-fx
  :ui/set-shield!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword)(opt) shield-id
  ; @param (map) shield-props
  ;  {:content (*)}
  ;
  ; @usage
  ;  [:ui/set-shield! {...}]
  ;
  ; @usage
  ;  [:ui/set-shield! :my-shield {...}]
  [a/event-vector<-id]
  (fn [_ [_ shield-id shield-props]]
      {:dispatch-later [{:ms   0 :dispatch [:ui/empty-shield!]}
                        {:ms  50 :dispatch {:ui/render-shield-content! shield-props}}
                        {:ms 100 :dispatch [:ui/show-shield!]}]}))

(a/reg-event-fx
  :ui/remove-shield!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  {:dispatch-later [{:ms                               0 :dispatch [:ui/hide-shield!]}
                    {:ms renderer/HIDE-ANIMATION-TIMEOUT :dispatch [:ui/empty-shield!]}]})

(a/reg-event-fx
  :ui/empty-shield!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  {:environment/empty-element! "x-app-shield--content"})
