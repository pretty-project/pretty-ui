
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-extensions.clients.client-lister.lifecycles
    (:require [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:router/add-route! :clients.client-lister/route
                                       {:route-template "/@app-home/clients"
                                        :client-event   [:clients.client-lister/load-lister!]
                                        :restricted?    true}]})
