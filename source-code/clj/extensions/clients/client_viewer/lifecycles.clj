
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-viewer.lifecycles
    (:require [plugins.item-viewer.api]
              [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:item-viewer/init-viewer! :clients.client-viewer
                                              {:base-route      "/@app-home/clients"
                                               :collection-name "clients"
                                               :handler-key     :clients.client-viewer
                                               :item-namespace  :client
                                               :on-route        [:clients.client-viewer/load-viewer!]
                                               :route-title     :clients}]})
