
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-extensions.clients.client-lister.lifecycles
    (:require [plugins.item-lister.api]
              [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :clients :client
                                              {:on-load [:clients.client-lister/load-lister!]
                                               :route-template "/@app-home/clients"}]})
