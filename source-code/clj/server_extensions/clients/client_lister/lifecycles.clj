
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-extensions.clients.client-lister.lifecycles
    (:require [server-plugins.item-lister.api :as item-lister]
              [x.server-core.api              :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :clients :client
                                              {:search-keys [:name :email-address]}]})
