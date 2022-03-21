
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-lister.lifecycles
    (:require [plugins.item-lister.api]
              [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot {:dispatch-n [[:environment/add-css! {:uri "/css/extensions/clients.client-lister.css"}]
                                 [:item-lister/init-lister! :clients :client
                                                            {:handler-key :clients.client-lister
                                                             :on-route [:clients.client-lister/load-lister!]
                                                             :route-template "/@app-home/clients"
                                                             :route-title :clients}]]}})
