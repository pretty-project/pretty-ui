
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-media.thumbnail-handler.lifecycles
    (:require [x.server-core.api                       :as a]
              [x.server-media.thumbnail-handler.routes :as thumbnail-handler.routes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-init [:router/add-route! :media/download-thumbnail
                                       {:route-template "/media/thumbnails/:filename"
                                        :get {:handler thumbnail-handler.routes/download-thumbnail}}]})
