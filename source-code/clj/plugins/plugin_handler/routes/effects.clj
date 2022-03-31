
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.routes.effects
    (:require [plugins.plugin-handler.routes.helpers :as routes.helpers]
              [x.server-core.api                     :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :plugin-handler/add-base-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (map) plugin-props
  ;  {:base-route (string)
  ;   :client-event (metamorphic-event)
  ;   parent-route (string)(opt)}
  (fn [_ [_ plugin-id {:keys [base-route client-event parent-route]}]]
      [:router/add-route! (routes.helpers/route-id plugin-id :base)
                          {:client-event   client-event
                           :route-parent   parent-route
                           :route-template base-route
                           :restricted?    true}]))

(a/reg-event-fx
  :plugin-handler/add-extended-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (map) plugin-props
  ;  {:client-event (metamorphic-event)
  ;   :extended-route (string)
  ;   :parent-route (string)(opt)}
  (fn [_ [_ plugin-id {:keys [client-event extended-route parent-route]}]]
      [:router/add-route! (routes.helpers/route-id plugin-id :extended)
                          {:client-event   client-event
                           :route-parent   parent-route
                           :route-template extended-route
                           :restricted?    true}]))
