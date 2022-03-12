
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.view-selector.route-handler.effects
    (:require [server-plugins.view-selector.route-handler.engine :as route-handler.engine]
              [x.server-core.api                                 :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :view-selector/add-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  ;  {:base-route (string)}
  (fn [_ [_ extension-id {:keys [base-route]}]]
      (if routed? [:router/add-route! (route-handler.engine/route-id extension-id :base)
                                      {:client-event   [:view-selector/load-selector! extension-id]
                                       :route-template base-route
                                       :restricted?    true}])))

(a/reg-event-fx
  :view-selector/add-extended-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  ;  {:extended-route (string)}
  (fn [_ [_ extension-id {:keys [extended-route]}]]
      (if routed? [:router/add-route! (route-handler.engine/route-id extension-id :extended)
                                      {:client-event   [:view-selector/load-selector! extension-id]
                                       :route-template extended-route
                                       :restricted?    true}])))
