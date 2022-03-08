
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-extensions.clients.client-lister.lifecycles
    (:require [server-plugins.item-lister.api]
              [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :clients :client
                                              {:base-route "/@app-home/clients"
                                               :on-load    [:clients.client-lister/load-lister!]}]})
