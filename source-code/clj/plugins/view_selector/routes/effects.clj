
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.routes.effects
    (:require [mid-fruits.uri                       :as uri]
              [plugins.view-selector.routes.helpers :as routes.helpers]
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
      (let [base-route   (uri/valid-path      base-route)
            parent-route (uri/uri->parent-uri base-route)]
           [:router/add-route! (routes.helpers/route-id selector-id :base)
                               {:client-event   [:view-selector/handle-route! selector-id]
                                :restricted?    true
                                :route-parent   parent-route
                                :route-template base-route}])))

(a/reg-event-fx
  :view-selector/add-extended-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;  {:base-route (string)}
  (fn [_ [_ selector-id {:keys [base-route]}]]
      (let [base-route     (uri/valid-path      base-route)
            extended-route (str                 base-route "/:view-id")
            parent-route   (uri/uri->parent-uri base-route)]
           [:router/add-route! (routes.helpers/route-id selector-id :extended)
                               {:client-event   [:view-selector/handle-route! selector-id]
                                :restricted?    true
                                :route-parent   parent-route
                                :route-template extended-route}])))
