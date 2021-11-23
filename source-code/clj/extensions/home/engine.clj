
(ns extensions.clients.engine
    (:require [x.server-core.api :as a]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:router/add-route! ::route
                                    {:route-template "/"
                                     :client-event   [:home/load!]
                                     :restricted?    true}]})
