
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.routes.effects
    (:require [plugins.item-browser.routes.helpers :as routes.helpers]
              [x.server-core.api                   :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/add-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  ;  {:base-route (string)}
  (fn [_ [_ extension-id item-namespace {:keys [base-route]}]]
      [:router/add-base-route! (routes.helpers/route-id extension-id item-namespace :base)
                               {:client-event   [:item-browser/handle-route! extension-id item-namespace]
                                :route-template base-route
                                :restricted?    true}]))

(a/reg-event-fx
  :item-browser/add-extended-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  ;  {:extended-route (string)}
  (fn [_ [_ extension-id item-namespace {:keys [extended-route]}]]
      [:router/add-route! (routes.helpers/route-id extension-id item-namespace :extended)
                          {:client-event   [:item-browser/handle-route! extension-id item-namespace]
                           :route-template extended-route
                           :restricted?    true}]))
