
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-lister.route-handler.effects
    (:require [server-plugins.item-lister.route-handler.engine :as route-handler.engine]
              [x.server-core.api                               :as a]))



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
      [:router/add-route! (route-handler.engine/route-id extension-id item-namespace)
                          {:client-event   [:item-lister/load-lister! extension-id item-namespace]
                           :route-template route-template
                           :restricted?    true}]))
