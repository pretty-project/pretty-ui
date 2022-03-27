
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.routes.effects
    (:require [plugins.plugin-handler.routes.effects]
              [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/add-base-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) lister-props
  ;  {:route-template (string)}
  (fn [_ [_ lister-id {:keys [route-template]}]]
      [:plugin-handler/add-base-route! lister-id
                                       {:client-event   [:item-lister/handle-route! lister-id]
                                        :route-template route-template}]))
