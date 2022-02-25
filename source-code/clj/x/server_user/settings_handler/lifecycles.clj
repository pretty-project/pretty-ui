
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.settings-handler.lifecycles
    (:require [x.server-core.api :as a]
              [x.server-user.settings-handler.routes :as settings-handler.routes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-init [:router/add-route! :user/upload-user-settings-item
                                       {:route-template "/user/upload-user-settings-item"
                                        :post {:handler settings-handler.routes/upload-user-settings-item!}}]})
