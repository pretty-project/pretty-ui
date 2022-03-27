
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.routes.effects
    (:require [plugins.plugin-handler.routes.effects]
              [x.server-core.api :as a]))



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
      [:plugin-handler/add-base-route! selector-id
                                       {:base-route   base-route
                                        :client-event [:view-selector/handle-route! selector-id]}]))

(a/reg-event-fx
  :view-selector/add-extended-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;  {:extended-route (string)}
  (fn [_ [_ selector-id {:keys [extended-route]}]]
      [:plugin-handler/add-extended-route! selector-id
                                           {:client-event   [:view-selector/handle-route! selector-id]
                                            :extended-route extended-route}]))
