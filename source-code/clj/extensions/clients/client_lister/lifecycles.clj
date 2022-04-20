
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-lister.lifecycles
    (:require [plugins.item-lister.api]
              [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :clients.client-lister
                                              {:base-route      "/@app-home/clients"
                                               :collection-name "clients"
                                               :handler-key     :clients.client-lister
                                               :item-namespace  :client
                                               :on-route        [:clients.client-lister/load-lister!]
                                               :route-title     :clients}]})
