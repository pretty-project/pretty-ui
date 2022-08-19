
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.routes.effects
    (:require [mid-fruits.uri                      :as uri]
              [plugins.item-browser.routes.helpers :as routes.helpers]
              [x.server-core.api                   :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/add-base-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) browser-props
  ;  {:base-route (string)}
  (fn [_ [_ browser-id {:keys [base-route]}]]
      (let [base-route   (uri/valid-path      base-route)
            parent-route (uri/uri->parent-uri base-route)]
           [:router/add-route! (routes.helpers/route-id browser-id :base)
                               {:client-event   [:item-browser/handle-route! browser-id]
                                :route-parent   parent-route
                                :restricted?    true
                                :route-template base-route}])))

(a/reg-event-fx
  :item-browser/add-extended-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) browser-props
  ;  {:base-route (string)}
  (fn [_ [_ browser-id {:keys [base-route]}]]
      (let [base-route     (uri/valid-path      base-route)
            extended-route (str                 base-route "/:item-id")
            parent-route   (uri/uri->parent-uri base-route)]
           [:router/add-route! (routes.helpers/route-id browser-id :extended)
                               {:client-event   [:item-browser/handle-route! browser-id]
                                :route-parent   parent-route
                                :restricted?    true
                                :route-template extended-route}])))
