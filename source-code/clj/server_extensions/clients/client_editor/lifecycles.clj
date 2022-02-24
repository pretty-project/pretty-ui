
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-extensions.clients.client-editor.lifecycles
    (:require [x.server-core.api :as a]
              [server-plugins.item-editor.api :as item-editor]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:item-editor/initialize-editor! :clients :client
                                                    {:suggestion-keys [:city]}]})
