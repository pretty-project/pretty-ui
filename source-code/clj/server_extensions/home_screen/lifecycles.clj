
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-extensions.home-screen.lifecycles
    (:require [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:router/add-route! :home-screen/route
                                       {:route-template "/@app-home"
                                        :client-event   [:home-screen/load!]
                                        :restricted?    true}]})
