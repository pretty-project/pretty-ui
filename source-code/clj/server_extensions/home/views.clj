
(ns server-extensions.home.views
    (:require [x.server-core.api :as a]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-server-boot [:router/add-route! :home/route
                                       {:route-template "/@app-home"
                                        :client-event   [:home/load!]
                                        :restricted?    true}]})
