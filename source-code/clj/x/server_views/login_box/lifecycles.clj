
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-views.login-box.lifecycles
    (:require [x.server-core.api :as a]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:router/add-route! :login-box/route
                                       {:client-event   [:views/render-login-box!]
                                        :route-template "/login"}]})
