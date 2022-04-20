
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.routes.effects
    (:require [mid-fruits.uri                     :as uri]
              [plugins.item-lister.routes.helpers :as routes.helpers]
              [x.server-core.api                  :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/add-base-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) lister-props
  ;  {:base-route (string)}
  (fn [_ [_ lister-id {:keys [base-route]}]]
      (let [base-route (uri/valid-path base-route)]
           [:router/add-route! (routes.helpers/route-id lister-id :base)
                               {:client-event   [:item-lister/handle-route! lister-id]
                                :restricted?    true
                                :route-template base-route}])))
