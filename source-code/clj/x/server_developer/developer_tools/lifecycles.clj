
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-developer.developer-tools.lifecycles
    (:require [x.server-core.api                         :as a]
              [x.server-developer.developer-tools.routes :as developer-tools.routes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot {:dispatch-if [(= (System/getenv "DEVELOPER") "true")
                                  [:router/add-routes! {:developer/developer-tools-route
                                                        {:route-template "/developer-tools"
                                                         :get (fn [request] (developer-tools.routes/download-developer-tools request))}
                                                        :developer/developer-tools-extended-route
                                                        {:route-template "/developer-tools/:tool-id"
                                                         :get (fn [request] (developer-tools.routes/download-developer-tools request))}}]]}})
