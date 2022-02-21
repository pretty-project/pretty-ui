
(ns server-extensions.home-screen.views
    (:require [x.server-core.api :as a]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:router/add-route! :home-screen/route
                                       {:route-template "/@app-home"
                                        :client-event   [:home-screen/load!]
                                        :restricted?    true}]})
