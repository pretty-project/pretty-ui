
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-views.login-box.lifecycles
    (:require [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot {:dispatch-n [[:environment/add-css! {:uri "/css/views/login-box.css"}]
                                 [:router/add-route! :login-box/route
                                                     {:client-event   [:views/render-login-box!]
                                                      :route-template "/login"}]]}})
