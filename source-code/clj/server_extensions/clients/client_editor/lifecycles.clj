
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-extensions.clients.client-editor.lifecycles
    (:require [server-plugins.item-editor.api]
              [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:item-editor/init-editor! :clients :client
                                              {:on-load [:clients.client-editor/load-editor!]
                                               :route-template "/@app-home/clients/:item-id"}]})
