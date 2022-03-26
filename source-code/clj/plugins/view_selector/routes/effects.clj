
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.routes.effects
    (:require [plugins.view-selector.routes.helpers :as routes.helpers]
              [x.server-core.api                    :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :view-selector/add-base-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;  {:base-route (string)}
  (fn [_ [_ selector-id {:keys [base-route]}]]
      [:router/add-route! (routes.helpers/route-id selector-id :base)
                          {:client-event   [:view-selector/handle-route! selector-id]
                           :route-template base-route
                           :restricted?    true}]))

(a/reg-event-fx
  :view-selector/add-extended-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;  {:extended-route (string)}
  (fn [_ [_ selector-id {:keys [extended-route]}]]
      [:router/add-route! (routes.helpers/route-id selector-id :extended)
                          {:client-event   [:view-selector/handle-route! selector-id]
                           :route-template extended-route
                           :restricted?    true}]))
