
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.routes.effects
    (:require [plugins.plugin-handler.routes.effects]
              [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/add-base-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) browser-props
  ;  {:base-route (string)
  ;   :parent-route (string)}
  (fn [_ [_ browser-id {:keys [base-route parent-route]}]]
      [:plugin-handler/add-base-route! browser-id
                                       {:base-route   base-route
                                        :parent-route parent-route
                                        :client-event [:item-browser/handle-route! browser-id]}]))

(a/reg-event-fx
  :item-browser/add-extended-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) browser-props
  ;  {:extended-route (string)
  ;   :parent-route (string)}
  (fn [_ [_ browser-id {:keys [extended-route parent-route]}]]
      [:plugin-handler/add-extended-route! browser-id
                                          {:client-event   [:item-browser/handle-route! browser-id]
                                           :extended-route extended-route
                                           :parent-route   parent-route}]))
