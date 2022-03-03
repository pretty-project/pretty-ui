
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-extensions.clients.client-editor.lifecycles
    (:require [server-plugins.item-editor.api :as item-editor]
              [x.server-core.api              :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:item-editor/init-editor! :clients :client
                                              {:suggestion-keys [:city]}]})
