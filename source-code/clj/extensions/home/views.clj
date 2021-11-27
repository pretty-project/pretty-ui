
(ns extensions.home.views
    (:require [x.server-core.api :as a]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:router/add-route! :home/route
                                    {:route-template "/"
                                     :client-event   [:home/load!]
                                     :restricted?    true}]})
