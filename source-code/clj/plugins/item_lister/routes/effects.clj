
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.routes.effects
    (:require [plugins.item-lister.routes.helpers :as routes.helpers]
              [x.server-core.api                  :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/add-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  ;  {:route-template (string)}
  (fn [_ [_ extension-id item-namespace {:keys [route-template]}]]
      [:router/add-route! (routes.helpers/route-id extension-id item-namespace)
                          {:client-event   [:item-lister/load-lister! extension-id item-namespace]
                           :route-template route-template
                           :restricted?    true}]))
