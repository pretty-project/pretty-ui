
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-sync.query-handler.lifecycles
    (:require [x.server-core.api                  :as a]
              [x.server-sync.query-handler.routes :as query-handler.routes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-init [:router/add-route! :sync/process-query!
                                       {:route-template "/query"
                                        :post {:handler query-handler.routes/process-query!}}]})
