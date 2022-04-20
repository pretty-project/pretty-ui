
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-editor.lifecycles
    (:require [plugins.item-editor.api]
              [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:item-editor/init-editor! :clients.client-editor
                                              {:base-route      "/@app-home/clients"
                                               :collection-name "clients"
                                               :handler-key     :clients.client-editor
                                               :item-namespace  :client
                                               :on-route        [:clients.client-editor/load-editor!]
                                               :route-title     :clients}]})
