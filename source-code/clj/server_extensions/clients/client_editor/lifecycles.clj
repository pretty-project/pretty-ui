
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-extensions.clients.client-editor.lifecycles
    (:require [x.server-core.api :as a]
              [server-plugins.item-editor.api :as item-editor]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:item-editor/init-editor! :clients :client
                                              {:suggestion-keys [:city]}]})
