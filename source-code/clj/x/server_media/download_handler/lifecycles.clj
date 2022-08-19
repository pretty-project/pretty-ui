
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-media.download-handler.lifecycles
    (:require [x.server-core.api                      :as a]
              [x.server-media.download-handler.routes :as download-handler.routes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-init [:router/add-route! :media/download-file
                                       {:route-template "/media/storage/:filename"
                                        :get {:handler download-handler.routes/download-file}}]})
