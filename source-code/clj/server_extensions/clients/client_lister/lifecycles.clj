
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-extensions.clients.client-lister.lifecycles
    (:require [x.server-core.api :as a]
              [server-plugins.item-lister.api :as item-lister]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :clients :client
                                              {:search-keys [:name :email-address]}]})
