
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-developer.docs.lifecycles
    (:require [x.server-core.api              :as a]
              [x.server-developer.docs.routes :as docs.routes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot {:dispatch-n [[:router/add-route! :docs/download-route
                                                     {:route-template "/docs/download-docs"
                                                      :get            #(docs.routes/download-docs %)}]
                                 [:router/add-route! :docs/route
                                                     {:route-template "/@app-home/docs"
                                                      :client-event   [:developer/load-docs!]
                                                      :restricted?    true}]]}})
