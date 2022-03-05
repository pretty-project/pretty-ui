
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-extensions.clients.client-editor.lifecycles
    (:require [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:router/add-route! :clients.client-editor/route
                                       {:route-template "/@app-home/clients/:client-id"
                                        :client-event   [:clients.client-editor/load-editor!]
                                        :restricted?    true}]})
