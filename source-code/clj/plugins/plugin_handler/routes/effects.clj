
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
  ;   :client-event (metamorphic-event)}
  (fn [_ [_ plugin-id {:keys [base-route client-event]}]]
      [:router/add-route! (routes.helpers/route-id plugin-id :base)
                          {:client-event   client-event
                           :route-template base-route
                           :restricted?    true}]))

(a/reg-event-fx
  :plugin-handler/add-extended-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (map) plugin-props
  ;  {:client-event (metamorphic-event)
  ;   :extended-route (string)}
  (fn [_ [_ plugin-id {:keys [client-event extended-route]}]]
      [:router/add-route! (routes.helpers/route-id plugin-id :extended)
                          {:client-event   client-event
                           :route-template extended-route
                           :restricted?    true}]))
