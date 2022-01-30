
(ns server-extensions.clients.client-lister.views
    (:require [x.server-core.api :as a]
              [server-plugins.item-lister.api :as item-lister]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:item-lister/initialize-lister! :clients :client
                                                    {:search-keys [:name :email-address]}]})
